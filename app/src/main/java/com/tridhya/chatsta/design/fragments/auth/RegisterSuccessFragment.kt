package com.tridhya.chatsta.design.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentRegisterSuccessBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.fragments.BaseFragment

class RegisterSuccessFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterSuccessBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnContinue.setOnClickListener(this)
        binding.tvSkip.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnContinue -> {
                preventDoubleClick(view)
                findNavController().navigate(R.id.to_cp_1)
            }

            R.id.tvSkip -> {
                preventDoubleClick(view)
                MessageDialog.getInstance(
                    requireContext(),
                    getString(R.string.if_you_don_t_enter_your_email_in_profile_information_you_won_t_be_able_to_reset_your_password),
                    R.style.ErrorThemeDialog
                )
                    .setIcon(R.drawable.ic_warning)
                    .setTitle(getString(R.string.please_note))
                    .setPositiveButtonText(R.string.continue_txt)
                    .setNegativeButton(R.string.skip)
                    .setListener(object : MessageDialog.ButtonListener {
                        override fun onPositiveBtnClicked(dialog: MessageDialog) {
                            dialog.dismiss()
                            findNavController().navigate(R.id.to_cp_1)
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
}