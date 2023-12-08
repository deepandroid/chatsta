package com.tridhya.chatsta.design.viewModel.chat

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.design.viewModel.BaseViewModel

class ChatViewModel(mContext: Context) : BaseViewModel(mContext) {
    val status: ObservableField<String> = ObservableField()
    val chatStatus: ObservableField<String> = ObservableField()
    val responseUserData: MutableLiveData<User> = MutableLiveData()
    val responseDonateReactions: MutableLiveData<Any?> = MutableLiveData()
    val responseBlockUnblockConnection: MutableLiveData<String> = MutableLiveData()

}