package com.tridhya.chatsta.design.dialogs

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutNotificationsDialogBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.design.viewModel.SettingsViewModel


class NotificationsBottomDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: LayoutNotificationsDialogBinding
    private val viewModel by lazy { SettingsViewModel(requireContext()) }
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
        binding = LayoutNotificationsDialogBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        val user = (context as ActivityBase).session?.user
        viewModel.messagesAlert.set(user?.messageAlerts)
        viewModel.messagesPreview.set(user?.messagePreview)
        viewModel.vibration.set(user?.vibration)
        viewModel.likes.set(user?.likes)
        viewModel.comments.set(user?.comments)
        binding.viewModel = viewModel
        setObservers()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//        mDBReference = AppClass.mDBReference
        binding.ivClose.setOnClickListener(this)
        binding.swMessageAlerts.setOnClickListener(this)
        binding.swVibrations.setOnClickListener(this)
        binding.swMessagePreview.setOnClickListener(this)
        binding.swLikes.setOnClickListener(this)
        binding.swComments.setOnClickListener(this)
    }

    private fun setObservers() {
        /*viewModel.responseUpdate.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            val user = (context as BaseActivity).session?.user
            user?.messageAlerts = it.data?.messageAlerts
            user?.vibration = it.data?.vibration
            user?.messagePreview = it.data?.messagePreview
            user?.likes = it.data?.likes
            user?.comments = it.data?.comments
            (context as BaseActivity).session?.user = user
            updateUserNotificationStatus()
            }
            */

    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                dialog?.dismiss()
            }

            R.id.swMessageAlerts, R.id.swMessagePreview, R.id.swVibrations, R.id.swLikes, R.id.swComments -> {
//                viewModel.isLoading.value = true
//                viewModel.notificationSettings((context as BaseActivity).session?.user?.userId)
            }
        }
    }
}