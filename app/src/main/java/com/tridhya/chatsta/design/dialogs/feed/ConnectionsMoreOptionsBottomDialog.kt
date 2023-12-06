package com.tridhya.chatsta.design.dialogs.feed

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.tridhya.chatsta.Model.User
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.LayoutConnectionsMoreOptionsDialogBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.provider.Constants.ALREADY_REQUESTED
import com.tridhya.chatsta.provider.Constants.CONNECTED
import com.tridhya.chatsta.provider.Constants.REQUEST_RECEIVED

class ConnectionsMoreOptionsBottomDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: LayoutConnectionsMoreOptionsDialogBinding

    companion object {
        private lateinit var listener: OptionSelectedListener
        private lateinit var userData: User
        private var isBlock: Boolean = false

        fun newInstance(
            userData: User,
            isBlock: Boolean,
            listener: OptionSelectedListener,
        ): ConnectionsMoreOptionsBottomDialog {
            Companion.listener = listener
            Companion.userData = userData
            Companion.isBlock = isBlock
            return ConnectionsMoreOptionsBottomDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutConnectionsMoreOptionsDialogBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isBlock) {
            binding.llBlock.gone()
            binding.llUnblock.visible()
            binding.llDisconnect.gone()
        } else {
            if (userData.userConnectionStatus == CONNECTED || userData.userConnectionStatus == ALREADY_REQUESTED || userData.userConnectionStatus == REQUEST_RECEIVED) {
                binding.llBlock.visible()
                binding.llUnblock.gone()
                binding.llDisconnect.visible()
            } else {
                binding.llBlock.visible()
                binding.llUnblock.gone()
                binding.llDisconnect.gone()
            }
        }
        when (userData.userConnectionStatus) {
            ALREADY_REQUESTED -> binding.tvDisconnect.text = getString(R.string.cancel_request)
            REQUEST_RECEIVED -> binding.tvDisconnect.text = getString(R.string.decline)
            else -> binding.tvDisconnect.text = getString(R.string.disconnect)
        }
        binding.llBlock.setOnClickListener(this)
        binding.llUnblock.setOnClickListener(this)
        binding.llDisconnect.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.llBlock -> {
                dismiss()
                listener.onBlockSelected(this, userData)
            }

            R.id.llUnblock -> {
                listener.onUnblockSelected(this, userData)
                dismiss()
            }

            R.id.llDisconnect -> {
                dismiss()
                listener.onDisconnectSelected(this, userData)
            }
        }
    }

    interface OptionSelectedListener {
        fun onBlockSelected(dialog: ConnectionsMoreOptionsBottomDialog, userData: User)
        fun onUnblockSelected(dialog: ConnectionsMoreOptionsBottomDialog, userData: User)
        fun onDisconnectSelected(dialog: ConnectionsMoreOptionsBottomDialog, userData: User)
    }
}