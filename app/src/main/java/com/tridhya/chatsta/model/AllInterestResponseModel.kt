package com.tridhya.chatsta.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AllInterestResponseModel(
    @SerializedName("_id")
    val interestId: String?,
    @SerializedName("interest")
    val interestName: String?,
    @SerializedName("__v")
    val interestValue: String?,
    var isSelected: Boolean = false,
) : Serializable