package com.tridhya.chatsta.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReactionPostResponseModel(
    @SerializedName("freereaction")
    @Expose
    var freereaction: Int? = 0,
    @SerializedName("totalDonation")
    @Expose
    var totalDonation: Double? = 0.0,
    @SerializedName("totalreaction")
    @Expose
    var totalReactionsCount: Long? = 0,
) : Serializable