package com.tridhya.chatsta.design.fragments.forogtPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentForgotPasswordBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.ForgotPasswordViewModel
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.isEmail
import com.tridhya.chatsta.extensions.visible

class ForgotPasswordFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel by lazy { ForgotPasswordViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.tvLoginTxt.setOnClickListener(this)
        binding.btnGetResetLink.setOnClickListener(this)

        setObservers()

        return binding.root
    }

    private fun setObservers() {
        viewModel.responseForgotPassword.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            binding.llForgotPassword.gone()
            binding.llForgotPasswordSuccess.visible()
        }
    }

    private fun validateEmail() {
        when {
            !viewModel.email.get().toString().trim().isEmail() -> {
                showToastShort(getString(R.string.err_enter_valid_email))
            }

            else -> {
//                viewModel.isLoading.value = true
//                viewModel.getResetLink()
                binding.llForgotPassword.gone()
                binding.llForgotPasswordSuccess.visible()
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvLoginTxt -> {
                preventDoubleClick(view)
                findNavController().popBackStack()
            }

            R.id.btnGetResetLink -> {
                preventDoubleClick(view)
                hideKeyboard()
                validateEmail()
            }
        }
    }
}