package com.tridhya.chatsta.design.viewModel

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.tridhya.chatsta.model.User

class RegisterViewModel(mContext: Context) : BaseViewModel(mContext) {

    val userData: MutableLiveData<User> = MutableLiveData()
    val userName: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()
    val confirmPassword: ObservableField<String> = ObservableField()
    val pin: ObservableField<String> = ObservableField()
//    private val authUseCase = AuthUseCase(mContext, errorLiveData, userData = userData)

    /*fun registerUser(
        invitePaidContentProvider: String? = null,
        reference: String? = null,
        token: String? = null,
    ) =
        authUseCase.register(
            RegisterRequestModel(
                userName = userName.get().toString().trim(),
                password = password.get().toString(),
                pin = pin.get().toString().trim(),
                invitePaidContentProvider = invitePaidContentProvider,
                reference = reference,
                token = token
            )
        )*/

}