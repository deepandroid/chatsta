package com.tridhya.chatsta.design.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.DialogChatMediaSelectorBinding
import com.tridhya.chatsta.enum.chat.MessageType

class SelectMediaDialog(
    context: Context,
    theme: Int,
) : Dialog(context, theme) {

    private lateinit var binding: DialogChatMediaSelectorBinding

    companion object {
        private var listener: OnMediaPickedListener? = null
        fun getInstance(
            context: Context,
            listener: OnMediaPickedListener,
            theme: Int = R.style.DefaultThemeDialog,
        ): SelectMediaDialog {
            this.listener = listener
            return SelectMediaDialog(
                context, theme
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        window?.setGravity(Gravity.BOTTOM)

        binding = DialogChatMediaSelectorBinding.inflate(
            LayoutInflater.from(context), null, false
        )
        setContentView(binding.root)

        binding.llAddPhotoVideo.setOnClickListener {
            listener?.onMediaPicked(this, MessageType.MEDIA)
        }

        binding.llAddMusic.setOnClickListener {
            listener?.onMediaPicked(this, MessageType.AUDIO)
        }

        binding.llAddPDF.setOnClickListener {
            listener?.onMediaPicked(this, MessageType.FILE)
        }

    }

    interface OnMediaPickedListener {
        fun onMediaPicked(dialog: SelectMediaDialog, mediaType: MessageType)
    }

}
