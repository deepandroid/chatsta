package com.tridhya.chatsta.design.fragments.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentRegisterBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.RegisterViewModel
import com.tridhya.chatsta.extensions.isValidPassword
import com.tridhya.chatsta.model.request.RegisterRequestModel
import com.tridhya.chatsta.provider.Constants.user1


class RegisterFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterBinding
    var isPaidContentProvider = false
    private var reference: String? = null
    private val viewModel by lazy { RegisterViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val arguments = arguments
        if (arguments != null) {
            isPaidContentProvider = arguments.getString("invitePaidContentProvider").toBoolean()
            reference = arguments.getString("reference").toString()
        }

        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLoginTxt.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.etPin.editText.setOnClickListener(this)

        binding.etUserName.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val result: String = s.toString().replace(" ", "_")
                if (s.toString() != result) {
                    binding.etUserName.editText.setText(result)
                    binding.etUserName.editText.setSelection(result.length)
                }
            }

        })

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvLoginTxt -> {
                preventDoubleClick(view)
                findNavController().popBackStack()
            }

            R.id.btnRegister -> {
                preventDoubleClick(view)
                validateRegister()
            }

            R.id.editText -> {
                preventDoubleClick(view)
                findNavController().navigate(R.id.to_createPin)
            }
        }
    }

    private fun validateRegister() {
        when {
            viewModel.userName.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_username))
            }

            viewModel.password.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_password))
            }

            !viewModel.password.get().toString().isValidPassword() -> {
                showToastShort(getString(R.string.err_enter_valid_password))
            }

            viewModel.confirmPassword.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_confirm_password))
            }

            viewModel.password.get().toString() != viewModel.confirmPassword.get().toString() -> {
                showToastShort(getString(R.string.err_password_not_match))
            }

            viewModel.pin.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_pin))
            }

            else -> {
                hideKeyboard()
                session?.user = user1
                session?.user?.username = viewModel.userName.get()
                session?.user?.password = viewModel.password.get()
                session?.user?.pin = viewModel.pin.get()
//                checkOrCreateNewUser(it)
                session?.savedLogin =
                    RegisterRequestModel(
                        userName = viewModel.userName.get().toString(),
                        password = viewModel.userName.get().toString(),
                        pin = viewModel.pin.get().toString()
                    )
                findNavController().navigate(R.id.to_register_success)
            }
        }
    }

    private fun initViews() {
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
        binding.etCreatePassword.editText.setOnFocusChangeListener { view, b ->
            binding.etCreatePassword.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    binding.etCreatePassword.ivPwdVisible.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    binding.etCreatePassword.ivPwdVisible.isSelected = b
                    b
                }
        }
        binding.etConfirmPassword.editText.setOnFocusChangeListener { view, b ->
            binding.etConfirmPassword.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    binding.etConfirmPassword.ivPwdVisible.isSelected = true
                    view.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    binding.etConfirmPassword.ivPwdVisible.isSelected = b
                    b
                }
        }
        binding.etCreatePassword.ivPwdVisible.setOnCheckedChangeListener { _, b ->
            binding.etCreatePassword.editText.transformationMethod =
                if (b) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
        }
        binding.etPin.editText.setOnFocusChangeListener { view, b ->
            binding.etPin.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    true
                } else {
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
        binding.etCreatePassword.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etCreatePassword.editText.isSelected = true
                binding.etCreatePassword.tvTitle.isSelected = true
                binding.etCreatePassword.ivPwdVisible.isSelected = true
            }
        }
        binding.etConfirmPassword.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etConfirmPassword.editText.isSelected = true
                binding.etConfirmPassword.tvTitle.isSelected = true
                binding.etConfirmPassword.ivPwdVisible.isSelected = true
            }
        }
        binding.etPin.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etPin.editText.isSelected = true
                binding.etPin.tvTitle.isSelected = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment
        ).currentBackStackEntry?.savedStateHandle
            ?.getLiveData<String>("pin")
            ?.observe(
                viewLifecycleOwner
            ) {
                if (!it.isNullOrBlank()) {
                    binding.etPin.value = it
                }
            }
    }
}