package com.tridhya.chatsta.model.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RegisterRequestModel(
    @SerializedName("username")
    val userName: String?,
    @SerializedName("password")
    var password: String?,
    @SerializedName("pin")
    var pin: String?,
    @SerializedName("invitePaidContentProvider")
    var invitePaidContentProvider: String? = null,
    @SerializedName("refrence")
    var reference: String? = null,
    @SerializedName("firebaseToken")
    var token: String? = null,
    @SerializedName("deviceType")
    val deviceType: String? = "A",
) : Serializable
