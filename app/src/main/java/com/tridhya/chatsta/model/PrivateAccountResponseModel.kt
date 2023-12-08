package com.tridhya.chatsta.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PrivateAccountResponseModel(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("isPrivateAccount")
    val isPrivateAccount: Boolean? = false,
) : Serializable
