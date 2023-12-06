package com.tridhya.chatsta.design.viewModel

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData

class ForgotPasswordViewModel(mContext: Context) : BaseViewModel(mContext) {

    val responseForgotPassword: MutableLiveData<String> = MutableLiveData()
    val responseSetNewPassword: MutableLiveData<String> = MutableLiveData()
    val email: ObservableField<String> = ObservableField()
    val newPassword: ObservableField<String> = ObservableField()
    val confirmNewPassword: ObservableField<String> = ObservableField()

}