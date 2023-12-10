package com.tridhya.chatsta.design.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentLoginBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.LoginViewModel
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.request.RegisterRequestModel
import com.tridhya.chatsta.provider.Constants.isShowed
import com.tridhya.chatsta.provider.Constants.user1

class LoginFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by lazy { LoginViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        checkLoggedIn()

        binding.tvForgotPassword.setOnClickListener(this)
        binding.tvSignUpTxt.setOnClickListener(this)
        binding.tvEnterPin.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        initViews()

        return binding.root
    }

    private fun checkLoggedIn() {
        if (session?.isLoggedIn == true) {
            findNavController().navigate(R.id.to_home)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvForgotPassword -> {
                preventDoubleClick(view)
                findNavController().navigate(R.id.to_forgot_pwd)
            }

            R.id.tvSignUpTxt -> {
                preventDoubleClick(view)
                findNavController().navigate(R.id.to_register)
            }

            R.id.tvEnterPin -> {
                preventDoubleClick(view)
                findNavController().navigate(R.id.to_pin)
            }

            R.id.btnLogin -> {
                preventDoubleClick(view)
                validateLogin()
            }
        }
    }

    private fun validateLogin() {
        when {
            viewModel.userName.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_username))
            }

            viewModel.password.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_password))
            }

            else -> {
                session?.user = user1
                session?.user?.username = viewModel.userName.get()
                session?.user?.password = viewModel.password.get()
                session?.savedLogin =
                    RegisterRequestModel(
                        userName = viewModel.userName.get(),
                        password = viewModel.userName.get(),
                        pin = session?.user?.pin,
                        invitePaidContentProvider = session?.user?.isPaidContentProvider.toString()
                    )
                viewModel.isLoading.value = false
                isShowed = true
//                checkOrCreateNewUser(data = it)
                findNavController().navigate(R.id.to_home)
            }
        }
    }


    private fun initViews() {

        if (session?.savedLogin != null) {
            viewModel.userName.set(session?.savedLogin?.userName)
            viewModel.password.set(session?.savedLogin?.password)
            binding.tvEnterPin.visible()
        } else {
            binding.tvEnterPin.gone()
        }

        binding.etUserName.editText.setOnFocusChangeListener { view, b ->
            binding.etUserName.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    b
                }
        }
        binding.etPassword.editText.setOnFocusChangeListener { view, b ->
            binding.etPassword.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    binding.etPassword.ivPwdVisible.isSelected = true
                    view.isSelected = true
                    true
                } else {
                    binding.etPassword.ivPwdVisible.isSelected = b
                    view.isSelected = b
                    b
                }
        }

        binding.etUserName.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etUserName.editText.isSelected = true
                binding.etUserName.tvTitle.isSelected = true
            }
        }
        binding.etPassword.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etPassword.editText.isSelected = true
                binding.etPassword.tvTitle.isSelected = true
                binding.etPassword.ivPwdVisible.isSelected = true
            }
        }
    }
}