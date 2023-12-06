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
import com.tridhya.chatsta.Model.response.EnumDataModel
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentCpMoreAboutYouBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.CompleteProfileViewModel

class CPMoreAboutYouFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentCpMoreAboutYouBinding
    private val viewModel by lazy { CompleteProfileViewModel(requireContext()) }
    private var genderList = arrayListOf<EnumDataModel>()
    private var relationshipList = arrayListOf<EnumDataModel>()
    private var starSignList = arrayListOf<EnumDataModel>()

    //    private lateinit var genderAdapter: EnumDataAdapter
//    private lateinit var relationshipAdapter: EnumDataAdapter
//    private lateinit var starSignAdapter: EnumDataAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentCpMoreAboutYouBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = viewModel

        }
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
    }

    private fun setObservers() {
        /*viewModel.responseUpdate.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.isLoading.value = false
                session?.user = it.data
                findNavController().navigate(R.id.to_cp_5)
                viewModel.responseUpdate.value = null
            }
        }*/
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        /* genderList = RelationshipStatusData.getGenderList(requireContext())
         if (session?.user?.gender != null) {
             for (i in genderList.indices) {
                 if (session?.user?.gender.equals(genderList[i].enum, true)) {
                     genderList[i].isSelected = true
                     viewModel.gender = genderList[i]
                 }
             }
         }

         relationshipList = RelationshipStatusData.getRelationshipList(requireContext())
         if (session?.user?.relationShipStatus != null) {
             for (i in relationshipList.indices) {
                 if (session?.user?.relationShipStatus.equals(relationshipList[i].enum, true)) {
                     relationshipList[i].isSelected = true
                     viewModel.relationshipStatus = relationshipList[i]
                 }
             }
         }

         starSignList = RelationshipStatusData.getStarSignList(requireContext())
         if (session?.user?.starSign != null) {
             for (i in starSignList.indices) {
                 if (session?.user?.starSign.equals(starSignList[i].enum, true)) {
                     starSignList[i].isSelected = true
                     viewModel.starSign = starSignList[i]
                 }
             }
         }*/

        if (session?.user?.bio.isNullOrBlank()) {
            viewModel.bio.set(session?.user?.bio)
        }

        binding.llBio.editText.setOnTouchListener { view, event ->
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

        binding.llBio.editText.setOnFocusChangeListener { view, b ->
            binding.llBio.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    b
                }
        }

        binding.llBio.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.llBio.editText.isSelected = true
                binding.llBio.tvTitle.isSelected = true
            }
        }
        /*genderAdapter = EnumDataAdapter(genderList, object : EnumDataAdapter.OnItemClickListener {
            override fun onItemSelected(data: EnumDataModel) {
                binding.llGender.recyclerview.isSelected = true
                binding.llGender.tvTitle.isSelected = true
                viewModel.gender = data
            }
        })*/
        binding.llGender.recyclerview.layoutManager = GridLayoutManager(context, 3)
//        binding.llGender.recyclerview.adapter = genderAdapter


        /* relationshipAdapter =
             EnumDataAdapter(relationshipList, object : EnumDataAdapter.OnItemClickListener {
                 override fun onItemSelected(data: EnumDataModel) {
                     binding.llRelationshipStatus.recyclerview.isSelected = true
                     binding.llRelationshipStatus.tvTitle.isSelected = true
                     viewModel.relationshipStatus = data
                 }
             })*/
        binding.llRelationshipStatus.recyclerview.layoutManager = GridLayoutManager(context, 3)
//        binding.llRelationshipStatus.recyclerview.adapter = relationshipAdapter

        /* starSignAdapter =
             EnumDataAdapter(starSignList, object : EnumDataAdapter.OnItemClickListener {
                 override fun onItemSelected(data: EnumDataModel) {
                     binding.llStarSign.recyclerview.isSelected = true
                     binding.llStarSign.tvTitle.isSelected = true
                     viewModel.starSign = data
                 }
             })*/
        binding.llStarSign.recyclerview.layoutManager = GridLayoutManager(context, 4)
//        binding.llStarSign.recyclerview.adapter = starSignAdapter
    }

//    private fun validate() {
//        when {
//            viewModel.gender == null && viewModel.relationshipStatus == null && viewModel.starSign == null && viewModel.bio.get()
//                .isNullOrBlank() -> {
//                findNavController().navigate(R.id.to_cp_5)
//            }
//            else -> {
//                viewModel.isLoading.value = true
//                setObservers()
//                session?.user?.userId?.let { viewModel.updateStep4(it) }
//            }
//        }
//    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnNext -> {
                preventDoubleClick(view)
//                validate()
                viewModel.isLoading.value = true
                setObservers()
                session?.user?.userId?.let {
                    findNavController().navigate(R.id.to_cp_5)
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