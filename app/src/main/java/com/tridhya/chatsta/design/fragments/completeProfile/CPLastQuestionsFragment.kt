package com.tridhya.chatsta.design.fragments.completeProfile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.tridhya.chatsta.Model.response.AllInterestResponseModel
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentCpLastQuestionsBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.dialogs.NumberPickerBottomDialog
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.CompleteProfileViewModel

class CPLastQuestionsFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentCpLastQuestionsBinding
    private var interestList: ArrayList<AllInterestResponseModel> = arrayListOf()
    private var selectedInterestList: ArrayList<String> = arrayListOf()

    //    private lateinit var interestAdapter: EnumInterestAdapter
    private var height = 50
    private val viewModel by lazy { CompleteProfileViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentCpLastQuestionsBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = viewModel
            getInterests()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        initViews()
    }

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    private fun initViews() {
        binding.etQuotes.editText.setOnTouchListener { view, event ->
            if (view.id == R.id.editText) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(
                        false
                    )
                }
            }
            false
        }

        if (session?.user?.height != null && session?.user?.height != 0) {
            viewModel.height.set(session?.user?.height.toString())
        }

        if (session?.user?.quotes.isNullOrBlank()) {
            viewModel.quotes.set(session?.user?.quotes)
        }

        binding.etHeight.editText.setOnClickListener {
            preventDoubleClick(it)
            if (binding.etHeight.value != null)
                height = binding.etHeight.value?.toInt()!!

            activity?.supportFragmentManager?.let { it1 ->
                NumberPickerBottomDialog.newInstance(
                    requireContext(),
                    height,
                    object : NumberPickerBottomDialog.OptionListener {
                        override fun onOptionSelected(view: View, selectedValue: Int) {
                            binding.etHeight.value = "$selectedValue"
                        }

                    }).show(it1, "dialog")
            }
        }

        binding.etHeight.editText.addTextChangedListener {
            if (it.toString().isNotBlank() && it.toString().toInt() > 0) {
                binding.etHeight.editText.isSelected = true
                binding.etHeight.tvTitle.isSelected = true
                binding.etHeight.ivDownArrow.isSelected = true
            }
        }

        binding.etQuotes.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etQuotes.editText.isSelected = true
                binding.etQuotes.tvTitle.isSelected = true
            }
        }

        binding.etQuotes.editText.setOnFocusChangeListener { view, b ->
            binding.etQuotes.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    b
                }
        }
    }

    private fun getInterests() {
        viewModel.allInterestList.observe(viewLifecycleOwner) { allInterestList ->
            viewModel.isLoading.value = false
            if (session?.user?.interests?.size!! > 0) {
                for (i in session?.user?.interests?.indices!!) {
                    for (j in allInterestList.indices) {
                        if (session?.user?.interests!![i].interestId == allInterestList[j].interestId) {
                            allInterestList[j].isSelected = true
                        }
                    }
                }
            }
            interestList = allInterestList
            /*interestAdapter =
                EnumInterestAdapter(interestList, object : EnumInterestAdapter.OnItemClickListener {
                    override fun onItemSelected(data: AllInterestResponseModel) {
                        if (interestAdapter.getSelectedCount() == 0) {
                            binding.llInterest.recyclerview.isSelected = false
                            binding.llInterest.tvTitle.isSelected = false
                        } else {
                            binding.llInterest.recyclerview.isSelected = true
                            binding.llInterest.tvTitle.isSelected = true
                        }
                    }
                }, true)*/
            binding.llInterest.recyclerview.layoutManager = GridLayoutManager(context, 2)
//            binding.llInterest.recyclerview.adapter = interestAdapter
        }
        viewModel.isLoading.value = true

//        viewModel.interestList()
    }

    /*private fun setObservers() {
        viewModel.responseUpdate.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.isLoading.value = false
                session?.user = it.data
                findNavController().navigate(R.id.to_register_complete_fragment)
                viewModel.responseUpdate.value = null
            }
        }
    }*/

    /* private fun validate() {
         when {
             (viewModel.height.get().isNullOrEmpty() || viewModel.height.get() == "0")
                     && getSelectedInterests().size < 1
                     && viewModel.quotes.get().isNullOrEmpty() -> {
                 findNavController().navigate(R.id.to_register_complete_fragment)
             }
             else -> {
                 viewModel.isLoading.value = true
                 setObservers()
                 session?.user?.userId?.let { viewModel.updateStep5(it, selectedInterestList) }
             }
         }
     }*/

    private fun getSelectedInterests(): ArrayList<String> {
        for (i in interestList.indices) {
            if (interestList[i].isSelected)
                interestList[i].interestId?.let { selectedInterestList.add(it) }
        }
        return selectedInterestList
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnNext -> {
                preventDoubleClick(view)
//                validate()
                viewModel.isLoading.value = true
//                setObservers()
                session?.user?.userId?.let {
//                    viewModel.updateStep5(it, getSelectedInterests())
                    findNavController().navigate(R.id.to_register_complete_fragment)
                }
            }

            R.id.ivClose -> {
                preventDoubleClick(view)
                MessageDialog.getInstance(
                    requireContext(),
                    getString(R.string.skip_complete_profile_confirm_txt)
                )
                    .setIcon(R.drawable.ic_delete_rounded)
                    .setPositiveButtonText(R.string.no)
                    .setNegativeButton(R.string.yes)
                    .setListener(object : MessageDialog.ButtonListener {
                        override fun onPositiveBtnClicked(dialog: MessageDialog) {
                            dialog.dismiss()
                        }

                        override fun onNegativeBtnClicked(dialog: MessageDialog) {
                            super.onNegativeBtnClicked(dialog)
                            val bundle = Bundle()
                            bundle.putBoolean("isRegister", true)
                            findNavController().navigate(R.id.to_home, bundle)
                        }
                    })
                    .show()
            }

            R.id.ivBack -> {
                preventDoubleClick(view)
                findNavController().popBackStack()
            }
        }
    }
}