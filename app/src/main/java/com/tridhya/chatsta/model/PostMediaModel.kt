package com.tridhya.chatsta.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostMediaModel(
    @SerializedName("type")
    @Expose
    val type: String? = null,
    @SerializedName("videoThumnail")
    @Expose
    val thumbnailUrl: String? = null,
    @SerializedName("videoDuration")
    @Expose
    val videoDuration: String? = null,
    @SerializedName("media")
    @Expose
    val url: String? = null,
    @SerializedName("_id")
    @Expose
    val id: String? = null,
) : Serializable