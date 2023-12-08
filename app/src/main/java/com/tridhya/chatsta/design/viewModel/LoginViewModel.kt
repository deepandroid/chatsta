package com.tridhya.chatsta.design.viewModel

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.tridhya.chatsta.model.User

class LoginViewModel(mContext: Context) : BaseViewModel(mContext) {

    val userData: MutableLiveData<User> = MutableLiveData()
    val userName: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()
//    private val authUseCase = AuthUseCase(mContext, errorLiveData, userData = userData)

    /*fun loginUser(token: String) = authUseCase.loginUser(
        LoginRequestModel(
            userName = userName.get().toString().trim(),
            password = password.get().toString(),
            token
        )
    )

    fun loginUser(userName: String, password: String, token: String) = authUseCase.loginUser(
        LoginRequestModel(
            userName = userName,
            password = password,
            token
        )
    )*/
}