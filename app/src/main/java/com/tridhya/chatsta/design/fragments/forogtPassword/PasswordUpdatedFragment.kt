package com.tridhya.chatsta.design.fragments.forogtPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentPasswordUpdatedBinding
import com.tridhya.chatsta.design.MainActivity
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.extensions.goToActivityAndClearTask

class PasswordUpdatedFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentPasswordUpdatedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPasswordUpdatedBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.btnLogin.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLogin -> {
                preventDoubleClick(view)
                goToActivityAndClearTask<MainActivity>()
            }
        }
    }

}