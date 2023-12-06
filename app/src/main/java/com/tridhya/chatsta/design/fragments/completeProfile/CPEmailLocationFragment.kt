package com.tridhya.chatsta.design.fragments.completeProfile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentCpEmailLocationBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.CompleteProfileViewModel
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.invisible
import com.tridhya.chatsta.extensions.isEmail
import com.tridhya.chatsta.extensions.visible

class CPEmailLocationFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentCpEmailLocationBinding
    private val viewModel by lazy { CompleteProfileViewModel(requireContext()) }
//    private var placesAutoCompleteAdapter: PlacesAutoCompleteAdapter? = null

    private var stopSearch = false

    private val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (!stopSearch) {
                if (s.toString() != "") {
//                    placesAutoCompleteAdapter?.filter?.filter(s.toString())
                    if (binding.rvLocations.visibility === View.INVISIBLE) {
                        binding.rvLocations.visible()
                    }
                } else {
                    if (binding.rvLocations.visibility == View.VISIBLE) {
                        binding.rvLocations.invisible()
                    }
                }
            } else {
                stopSearch = false
                if (binding.rvLocations.visibility == View.VISIBLE) {
                    binding.rvLocations.invisible()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentCpEmailLocationBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = viewModel
        }
//        Places.initialize(requireContext(), Constants.GOOGLE_MAP_KEYS)
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.etLocation.ivClear.setOnClickListener(this)
    }

    private fun setObservers() {
        /*viewModel.responseUpdate.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.isLoading.value = false
                session?.user = it.data
                it.data?.let { it1 -> updateFirebaseUser(it1) }
                findNavController().navigate(R.id.to_cp_3)
                viewModel.responseUpdate.value = null
            }
        }*/
    }

    private fun initViews() {

        binding.etLocation.editText.addTextChangedListener(filterTextWatcher)
//        placesAutoCompleteAdapter = PlacesAutoCompleteAdapter(requireContext(), this)
        binding.rvLocations.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvLocations.adapter = placesAutoCompleteAdapter
//        placesAutoCompleteAdapter?.notifyDataSetChanged()

        if (!session?.user?.email.isNullOrBlank())
            viewModel.email.set(session?.user?.email)
        if (!session?.user?.location.isNullOrBlank())
            viewModel.location.set(session?.user?.location)

        if (session?.user?.isPaidContentProvider == true)
            binding.etEmail.editText.isEnabled = false

        binding.etEmail.editText.setOnFocusChangeListener { view, b ->
            binding.etEmail.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    b
                }
        }
        binding.etLocation.editText.setOnFocusChangeListener { view, b ->
            binding.etLocation.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    binding.etLocation.ivClear.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    binding.etLocation.ivClear.isSelected = b
                    b
                }
        }

        binding.etLocation.editText.addTextChangedListener {
            if (it.toString().isBlank()) {
                binding.etLocation.ivClear.gone()
            } else {
                binding.etLocation.ivClear.visible()
                binding.etLocation.editText.isSelected = true
                binding.etLocation.tvTitle.isSelected = true
                binding.etLocation.ivClear.isSelected = true
            }
        }

        binding.etEmail.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etEmail.editText.isSelected = true
                binding.etEmail.tvTitle.isSelected = true
            }
        }
    }


    private fun validate() {
        when {
            viewModel.email.get().isNullOrBlank() && viewModel.location.get().isNullOrBlank() -> {
                showValidationDialog()
            }

            !viewModel.email.get().isNullOrBlank() && !viewModel.email.get().toString().trim()
                .isEmail() -> {
                showToastShort(getString(R.string.err_enter_valid_email))
            }

            !viewModel.location.get().isNullOrBlank() -> {
                if (viewModel.email.get().isNullOrBlank()) {
                    showValidationDialog()
                } else {
                    viewModel.isLoading.value = true
                    setObservers()
                    session?.user?.userId?.let {
                        findNavController().navigate(R.id.to_cp_3)
                    }
                }
            }

            else -> {
                viewModel.isLoading.value = true
                setObservers()
                session?.user?.userId?.let {
                    findNavController().navigate(R.id.to_cp_3)
                }
            }
        }
    }

    private fun showValidationDialog() {
        MessageDialog.getInstance(
            requireContext(),
            getString(R.string.if_you_don_t_enter_your_email_in_profile_information_you_won_t_be_able_to_reset_your_password),
            R.style.ErrorThemeDialog
        )
            .setIcon(R.drawable.ic_warning)
            .setTitle(getString(R.string.please_note))
            .setPositiveButtonText(R.string.continue_txt)
            .setNegativeButton(R.string.cancel)
            .setListener(object : MessageDialog.ButtonListener {
                override fun onPositiveBtnClicked(dialog: MessageDialog) {
                    dialog.dismiss()
                    viewModel.isLoading.value = true
                    setObservers()
                    session?.user?.userId?.let {
                        findNavController().navigate(R.id.to_cp_3)
                    }
                }

                override fun onNegativeBtnClicked(dialog: MessageDialog) {
                    super.onNegativeBtnClicked(dialog)
                    dialog.dismiss()
                }
            })
            .show()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnNext -> {
                preventDoubleClick(view)
                validate()
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

            R.id.ivClear -> {
                preventDoubleClick(view)
                binding.etLocation.editText.setText("")
            }
        }
    }


}