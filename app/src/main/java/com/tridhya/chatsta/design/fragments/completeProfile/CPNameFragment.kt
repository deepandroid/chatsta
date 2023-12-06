package com.tridhya.chatsta.design.fragments.completeProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentCpNameBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.CompleteProfileViewModel

class CPNameFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentCpNameBinding
    private val viewModel by lazy { CompleteProfileViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentCpNameBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = viewModel
            binding.btnNext.setOnClickListener(this)
            binding.ivClose.setOnClickListener(this)
        }
        initViews()
        return binding.root
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnNext -> {
                preventDoubleClick(view)
                viewModel.isLoading.postValue(true)
                setObservers()
                session?.user?.userId?.let {
//                    viewModel.updateStep1(it)
                    findNavController().navigate(R.id.to_cp_2)
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
        }
    }

    private fun setObservers() {
        /* viewModel.responseUpdate.observe(viewLifecycleOwner) {
             if (it != null) {
                 viewModel.isLoading.postValue(false)
                 session?.user = it.data
                 findNavController().navigate(R.id.to_cp_2)
                 viewModel.responseUpdate.value = null
             }
         }*/
    }

//    private fun validate() {
//        when {
//            viewModel.firstName.get().isNullOrBlank() && viewModel.lastName.get()
//                .isNullOrBlank() -> {
//                findNavController().navigate(R.id.to_cp_2)
//            }
//            else -> {
//                viewModel.isLoading.postValue(true)
//                setObservers()
//                session?.user?.userId?.let { viewModel.updateStep1(it) }
//            }
//        }
//    }

    private fun initViews() {
        if (!session?.user?.firstName.isNullOrBlank())
            viewModel.firstName.set(session?.user?.firstName)
        if (!session?.user?.lastName.isNullOrBlank())
            viewModel.lastName.set(session?.user?.lastName)

        binding.etFirstName.editText.setOnFocusChangeListener { view, b ->
            binding.etFirstName.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    b
                }
        }
        binding.etFirstName.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etFirstName.editText.isSelected = true
                binding.etFirstName.tvTitle.isSelected = true
            }
        }
        binding.etLastName.editText.setOnFocusChangeListener { view, b ->
            binding.etLastName.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    b
                }
        }
        binding.etLastName.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etLastName.editText.isSelected = true
                binding.etLastName.tvTitle.isSelected = true
            }
        }
    }
}