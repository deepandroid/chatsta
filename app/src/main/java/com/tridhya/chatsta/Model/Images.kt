package com.tridhya.chatsta.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Images(
    @SerializedName("image")
    var image: String?,
    @SerializedName("_id")
    var id: String?,
) : Serializable
