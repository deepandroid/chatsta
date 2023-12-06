package com.tridhya.chatsta.design.dialogs

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutSettingsDialogBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment


class SettingsBottomDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: LayoutSettingsDialogBinding
//    private val viewModel by lazy { MainViewModel(requireContext()) }
//    private var mDBReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutSettingsDialogBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//        mDBReference = AppClass.mDBReference
        setObservers()
        binding.tvSignOut.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        binding.tvAccount.setOnClickListener(this)
        binding.tvNotifications.setOnClickListener(this)
        binding.tvTransactions.setOnClickListener(this)
        binding.tvInformativePages.setOnClickListener(this)
        binding.tvMembership.setOnClickListener(this)
        /* if (session?.user?.totalIncome!! > 0 || session?.user?.totalDue!! > 0) {
             binding.tvTransactions.setCompoundDrawablesRelativeWithIntrinsicBounds(
                 R.drawable.ic_transactions_pending,
                 0,
                 0,
                 0
             )
             binding.tvTransactions.compoundDrawablePadding = 16
         }*/
    }

    private fun setObservers() {
        /*viewModel.responseLogout.observe(requireActivity()) {
            viewModel.isLoading.value = false
            updateUserStatus(UserStatus.OFFLINE)
            (context as BaseActivity).session?.logout()
//            goToActivityAndClearTask<MainActivity>()
            val intent = Intent(requireActivity(), MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            requireActivity().startActivity(intent)
            requireActivity().finish()

        }
*/
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvSignOut -> {
                (context as ActivityBase).preventDoubleClick(view)
//                viewModel.isLoading.value = true
//                viewModel.logoutUser()
            }

            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                dismiss()
            }

            R.id.tvAccount -> {
                (context as ActivityBase).preventDoubleClick(view)
                dismiss()
                findNavController().navigate(R.id.account)
            }

            R.id.tvNotifications -> {
                (context as ActivityBase).preventDoubleClick(view)
                findNavController().navigate(R.id.notificationsSettingDialog)
            }

            R.id.tvInformativePages -> {
                (context as ActivityBase).preventDoubleClick(view)
                findNavController().navigate(R.id.informativePagesDialog)
            }

            R.id.tvTransactions -> {
                (context as ActivityBase).preventDoubleClick(view)
                findNavController().navigate(R.id.to_transactions)
            }

            R.id.tvMembership -> {
                (context as ActivityBase).preventDoubleClick(view)
                findNavController().navigate(R.id.membershipBottomDialog)
            }
        }
    }
}