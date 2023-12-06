package com.tridhya.chatsta.Model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChangeCredentialsResponseModel(
    @SerializedName("pin")
    val pin: String?,
    @SerializedName("password")
    val password: String?,
) : Serializable