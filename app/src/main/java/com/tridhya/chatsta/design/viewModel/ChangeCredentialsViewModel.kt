package com.tridhya.chatsta.design.viewModel

import android.content.Context
import androidx.databinding.ObservableField

class ChangeCredentialsViewModel(mContext: Context) : BaseViewModel(mContext) {
    /*val responseChangePassword: MutableLiveData<BaseModel<ChangeCredentialsResponseModel>> =
        MutableLiveData()
    val responseChangePin: MutableLiveData<BaseModel<ChangeCredentialsResponseModel>> =
        MutableLiveData()
    */val oldPassword: ObservableField<String> = ObservableField()
    val newPassword: ObservableField<String> = ObservableField()
    val confirmNewPassword: ObservableField<String> = ObservableField()
    val oldPin: ObservableField<String> = ObservableField()
    val newPin: ObservableField<String> = ObservableField()
    val email: ObservableField<String> = ObservableField()
    /*private val authUseCase =
        ProfileUseCase(
            mContext,
            errorLiveData,
            responseChangePassword = responseChangePassword,
            responseChangePin = responseChangePin
        )*/

    /* fun changePassword() = authUseCase.changePassword(
         ChangePasswordRequestModel(
             oldPassword = oldPassword.get().toString().trim(),
             newPassword = newPassword.get().toString().trim(),
         )
     )

     fun changePin() = authUseCase.changePin(
         ChangePinRequestModel(
             oldPin = oldPin.get().toString().trim(),
             newPin = newPin.get().toString().trim(),
         )
     )*/
}