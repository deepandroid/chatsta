package com.tridhya.chatsta.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InformativePagesInfoResponseModel(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("website")
    val website: String?,
    @SerializedName("aboutUsLink")
    val aboutUsLink: String?,
    @SerializedName("termsAndConditionLink")
    val termsAndConditionsLink: String?,
    @SerializedName("privacyLink")
    val privacyPolicyLink: String?,
    @SerializedName("version")
    val version: String?,
    @SerializedName("lastUpdate")
    val lastUpdate: String?,
    @SerializedName("generalInformation")
    val generalInformation: String?,
) : Serializable
