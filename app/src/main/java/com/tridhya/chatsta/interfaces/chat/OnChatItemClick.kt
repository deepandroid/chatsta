package com.tridhya.chatsta.interfaces.chat

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tridhya.chatsta.model.Message

interface OnChatItemClick {
    fun onClick(data: Message)
    fun onBurnListener(data: Message)
    fun onLongClick(
        data: Any,
        context: Context,
        ivAnimation: View,
        imageView: AppCompatImageView,
    )
}