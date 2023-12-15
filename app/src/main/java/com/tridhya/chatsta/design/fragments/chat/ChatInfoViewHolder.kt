package com.tridhya.chatsta.design.fragments.chat

import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.databinding.ItemGroupActivityBinding
import com.tridhya.chatsta.model.Message
import com.tridhya.chatsta.model.User

class ChatInfoViewHolder(var binder: ItemGroupActivityBinding) :
    RecyclerView.ViewHolder(binder.root) {
//    var mDBReference: DatabaseReference? = null

    fun bind(data: Message, user: User?, chatUserId: String?) {
//        mDBReference = AppClass.mDBReference
//        val aes = SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
//        val decryptedMessage = aes.decryption(data.message)

        if (user?.userId == chatUserId) {
            binder.tvText.text = "${user?.username} ${data.message}"
        } else {
            binder.tvText.text = " ${data.message}"

        }

    }
}