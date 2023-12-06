package com.tridhya.chatsta.design.viewModel

import android.content.Context
import androidx.databinding.ObservableField

class SettingsViewModel(mContext: Context) : BaseViewModel(mContext) {

    /*    val responseUpdate: MutableLiveData<BaseModel<User>> = MutableLiveData()
        val responseCMSInfo: MutableLiveData<BaseModel<InformativePagesInfoResponseModel>> =
            MutableLiveData()*/
//    private val responseCMSPages: MutableLiveData<BaseModel<String>> = MutableLiveData()
    val messagesAlert: ObservableField<Boolean> = ObservableField()
    val vibration: ObservableField<Boolean> = ObservableField()
    val messagesPreview: ObservableField<Boolean> = ObservableField()
    val likes: ObservableField<Boolean> = ObservableField()
    val comments: ObservableField<Boolean> = ObservableField()
    val email: ObservableField<String> = ObservableField()
    val version: ObservableField<String> = ObservableField()
    val lastUpdateDate: ObservableField<String> = ObservableField()
    val web: ObservableField<String> = ObservableField()
    val generalInformation: ObservableField<String> = ObservableField()
    /*private val authUseCase =
        ProfileUseCase(
            mContext,
            errorLiveData,
            responseUpdateUser = responseUpdate,
            responseCMSPages = responseCMSPages,
            responseCMSPagesInfo = responseCMSInfo
        )

    fun notificationSettings(userId: String?) =
        authUseCase.notificationSettings(
            userId, UpdateUserProfileRequestModel(
                messageAlerts = messagesAlert.get(),
                messagePreview = messagesPreview.get(),
                vibration = vibration.get(),
                likes = likes.get(),
                comments = comments.get(),
            )
        )

    fun informativePagesInfo(type: String?) = authUseCase.informativePagesInfo(type)
*/
}