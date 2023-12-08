package com.tridhya.chatsta.design.fragments.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentRegisterCompleteBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.provider.Constants.isShowed

class RegisterCompleteFragment : BaseFragment() {

    private lateinit var binding: FragmentRegisterCompleteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterCompleteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            isShowed = false
            val bundle = Bundle()
            bundle.putBoolean("isRegister", true)
            findNavController().navigate(R.id.to_home, bundle)
        }, 3000)
    }
}