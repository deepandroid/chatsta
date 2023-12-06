package com.tridhya.chatsta.design.fragments.forogtPassword

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentEnterNewPasswordBinding
import com.tridhya.chatsta.design.MainActivity
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.ForgotPasswordViewModel
import com.tridhya.chatsta.extensions.goToActivityAndClearTask
import com.tridhya.chatsta.extensions.isValidPassword

class EnterNewPasswordFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentEnterNewPasswordBinding
    private val viewModel by lazy { ForgotPasswordViewModel(requireContext()) }
    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEnterNewPasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val arguments = arguments
        if (arguments != null) {
            token = arguments.getString("token")
        }
        initViews()
        setObservers()
        return binding.root
    }

    private fun initViews() {
        binding.llNewPassword.editText.setOnFocusChangeListener { view, b ->
            binding.llNewPassword.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    binding.llNewPassword.ivPwdVisible.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    binding.llNewPassword.ivPwdVisible.isSelected = b
                    b
                }
        }
        binding.llNewPassword.ivPwdVisible.setOnCheckedChangeListener { _, b ->
            binding.llNewPassword.editText.transformationMethod =
                if (b) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLoginTxt.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
    }

    private fun setObservers() {
        viewModel.responseSetNewPassword.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            findNavController().navigate(R.id.to_password_updated)
        }
    }

    private fun validatePassword() {
        when {
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
                .toString()
                .trim() -> {
                showToastShort(getString(R.string.err_password_not_match))
            }

            else -> {
//                viewModel.isLoading.value = true
                token?.let {
//                    viewModel.setNewPassword(it)
                    findNavController().navigate(R.id.to_password_updated)
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvLoginTxt -> {
                preventDoubleClick(view)
                goToActivityAndClearTask<MainActivity>()
            }

            R.id.btnSave -> {
                preventDoubleClick(view)
                validatePassword()
            }
        }
    }
}