package com.tridhya.chatsta.design.dialogs.account

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutChangePasswordBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.viewModel.ChangeCredentialsViewModel
import com.tridhya.chatsta.extensions.isValidPassword

class ChangePasswordDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: LayoutChangePasswordBinding
    private val viewModel by lazy { ChangeCredentialsViewModel(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutChangePasswordBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.viewModel = viewModel
        initViews()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        /*        viewModel.responseChangePassword.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    val savedLogin = (context as BaseActivity).session?.savedLogin
                    savedLogin?.password = it.data?.password
                    (context as BaseActivity).session?.savedLogin = savedLogin
                    MessageDialog.getInstance(
                        requireContext(),
                        it.message,
                        R.style.DefaultThemeDialog
                    )
                        .setIcon(R.drawable.ic_check_purple)
                        .setPositiveButtonText(R.string.ok)
                        .setListener(object : MessageDialog.ButtonListener {
                            override fun onPositiveBtnClicked(dialog: MessageDialog) {
                                dialog.dismiss()
                                dismiss()
                            }
                        })
                        .show()
                }*/
    }

    private fun initViews() {
        binding.etNewPassword.editText.setOnFocusChangeListener { view, b ->
            binding.etNewPassword.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    binding.etNewPassword.ivPwdVisible.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    binding.etNewPassword.ivPwdVisible.isSelected = b
                    b
                }
        }
        binding.etCurrentPassword.editText.setOnFocusChangeListener { view, b ->
            binding.etCurrentPassword.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    binding.etCurrentPassword.ivPwdVisible.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    binding.etCurrentPassword.ivPwdVisible.isSelected = b
                    b
                }
        }
        binding.etConfirmNewPassword.editText.setOnFocusChangeListener { view, b ->
            binding.etConfirmNewPassword.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    binding.etConfirmNewPassword.ivPwdVisible.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    binding.etConfirmNewPassword.ivPwdVisible.isSelected = b
                    b
                }
        }
        binding.etNewPassword.ivPwdVisible.setOnCheckedChangeListener { _, b ->
            binding.etNewPassword.editText.transformationMethod =
                if (b) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
        }

        binding.etNewPassword.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etNewPassword.editText.isSelected = true
                binding.etNewPassword.tvTitle.isSelected = true
                binding.etNewPassword.ivPwdVisible.isSelected = true
            }
        }

        binding.etCurrentPassword.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etCurrentPassword.editText.isSelected = true
                binding.etCurrentPassword.tvTitle.isSelected = true
                binding.etCurrentPassword.ivPwdVisible.isSelected = true
            }
        }

        binding.etConfirmNewPassword.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etConfirmNewPassword.editText.isSelected = true
                binding.etConfirmNewPassword.tvTitle.isSelected = true
                binding.etConfirmNewPassword.ivPwdVisible.isSelected = true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.ivClose.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                dismiss()
            }

            R.id.btnSave -> {
                (context as ActivityBase).preventDoubleClick(view)
                validate()
            }
        }
    }

    fun validate() {
        when {
            viewModel.oldPassword.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_password))
            }

            !viewModel.oldPassword.get().toString().trim().isValidPassword() -> {
                showToastShort(getString(R.string.err_enter_valid_password))
            }

            viewModel.newPassword.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_password))
            }

            !viewModel.newPassword.get().toString().trim().isValidPassword() -> {
                showToastShort(getString(R.string.err_enter_valid_password))
            }

            viewModel.confirmNewPassword.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_confirm_password))
            }

            viewModel.newPassword.get().toString().trim() != viewModel.confirmNewPassword.get()
                .toString().trim() -> {
                showToastShort(getString(R.string.err_password_not_match))
            }

            else -> {
//                viewModel.isLoading.value = true
//                viewModel.changePassword()
                val savedLogin = (context as ActivityBase).session?.savedLogin
//                savedLogin?.password = it.data?.password
                (context as ActivityBase).session?.savedLogin = savedLogin
                MessageDialog.getInstance(
                    requireContext(),
                    "it.message",
                    R.style.DefaultThemeDialog
                )
                    .setIcon(R.drawable.ic_check_purple)
                    .setPositiveButtonText(R.string.ok)
                    .setListener(object : MessageDialog.ButtonListener {
                        override fun onPositiveBtnClicked(dialog: MessageDialog) {
                            dialog.dismiss()
                            dismiss()
                        }
                    })
                    .show()
            }
        }
    }

}