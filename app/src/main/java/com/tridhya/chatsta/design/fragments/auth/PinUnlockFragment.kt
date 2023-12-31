package com.tridhya.chatsta.design.fragments.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentPinUnlockBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.LoginViewModel
import com.tridhya.chatsta.model.request.RegisterRequestModel
import com.tridhya.chatsta.provider.Constants

class PinUnlockFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentPinUnlockBinding
    private val viewModel by lazy { LoginViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPinUnlockBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv0.setOnClickListener(this)
        binding.tv1.setOnClickListener(this)
        binding.tv2.setOnClickListener(this)
        binding.tv3.setOnClickListener(this)
        binding.tv4.setOnClickListener(this)
        binding.tv5.setOnClickListener(this)
        binding.tv6.setOnClickListener(this)
        binding.tv7.setOnClickListener(this)
        binding.tv8.setOnClickListener(this)
        binding.tv9.setOnClickListener(this)
        binding.tvDelete.setOnClickListener(this)
        binding.tvUsePassword.setOnClickListener(this)

        binding.firstPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.firstPinView.text?.length == 4)
                    validatePin()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun validatePin() {
//        val aes = SecurePreferencesUtil(AES_KEY, AES_IV)
        val decryptedPin = session?.savedLogin?.pin
        val decryptedPassword = session?.savedLogin?.password

        Log.e("DecryptedPin", "decryptedPin : $decryptedPin")
        Log.e("DecryptedPassword", "decryptedPassword : $decryptedPassword")
        if (binding.firstPinView.text?.toString().equals(decryptedPin)) {
            session?.user = Constants.user1
//                    session?.user?.username = viewModel.userName.get()
//                    session?.user?.password = viewModel.password.get()
            session?.savedLogin =
                RegisterRequestModel(
                    userName = viewModel.userName.get(),
                    password = viewModel.userName.get(),
                    pin = session?.user?.pin,
                    invitePaidContentProvider = session?.user?.isPaidContentProvider.toString()
                )
            viewModel.isLoading.value = false
            Constants.isShowed = true
            findNavController().navigate(R.id.to_home)
        } else {
            val shakeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
            shakeAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    context?.getColor(R.color.red)?.let { binding.firstPinView.setTextColor(it) }
                }

                override fun onAnimationEnd(p0: Animation?) {
                    context?.getColor(R.color.purple)?.let { binding.firstPinView.setTextColor(it) }
                    binding.firstPinView.setText("")
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }

            })
            binding.firstPinView.startAnimation(shakeAnim)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv0 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "0")
            }

            R.id.tv1 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "1")
            }

            R.id.tv2 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "2")
            }

            R.id.tv3 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "3")
            }

            R.id.tv4 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "4")
            }

            R.id.tv5 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "5")
            }

            R.id.tv6 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "6")
            }

            R.id.tv7 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "7")
            }

            R.id.tv8 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "8")
            }

            R.id.tv9 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "9")
            }

            R.id.tvDelete -> {
                preventDoubleClick(view)
                if (!binding.firstPinView.text.isNullOrBlank()) {
                    var txt = binding.firstPinView.text.toString()
                    txt = txt.subSequence(0, txt.length - 1).toString()
                    binding.firstPinView.setText(txt)
                }
            }

            R.id.tvUsePassword -> {
                preventDoubleClick(view)
                findNavController().popBackStack()
            }
        }
    }

}