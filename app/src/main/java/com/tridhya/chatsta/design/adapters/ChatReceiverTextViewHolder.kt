package com.tridhya.chatsta.design.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.DialogChatMessageOptionsBinding
import com.tridhya.chatsta.databinding.ItemChatReceiverBinding
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.model.ChatModel
import com.tridhya.chatsta.provider.Constants.user1
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.TimeStamp

class ChatReceiverTextViewHolder(
    val binder: ItemChatReceiverBinding,
    val context: Context,
) :
    RecyclerView.ViewHolder(binder.root) {
    fun bind(message: ChatModel) {
        try {
            binder.tvChatMessage.text = message.message
        } catch (e: Exception) {
            binder.tvChatMessage.text = message.message
        }
        val time =
            TimeStamp.millisToFormat(message.chatTime.toString(), TimeStamp.SHORT_TIME_FORMAT)
        binder.tvTime.text = String.format("%s", time)
        GlideUtils(binder.ivProfileImage.context).circleImage()
            .loadImage(R.drawable.ic_app, binder.ivProfileImage)

        itemView.setOnLongClickListener {
            val popupBinding = DialogChatMessageOptionsBinding.inflate(LayoutInflater.from(context))
            val popup = PopupWindow(
                popupBinding.root,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            popup.animationStyle = com.chaos.view.R.style.Animation_AppCompat_Dialog
            popup.showAsDropDown(binder.container, 0, 0, Gravity.START)
            popupBinding.llWipe.gone()
            popupBinding.view2.gone()
            popupBinding.llSelfDestructTimeMain.gone()

            popupBinding.llCopy.setOnClickListener {
                popup.dismiss()
                /*val message = try {
//                    val aes = SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
//                    aes.decryption(data.message).toString()
                } catch (e: Exception) {
                    data.message.toString()
                }*/
                val clipboard: ClipboardManager? =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                val clip = ClipData.newPlainText("label", message.message)
                clipboard?.setPrimaryClip(clip)
            }
            return@setOnLongClickListener true
        }
    }
}