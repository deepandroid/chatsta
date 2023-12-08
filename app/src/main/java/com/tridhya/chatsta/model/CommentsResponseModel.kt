package com.tridhya.chatsta.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CommentsResponseModel(
    @SerializedName("comment")
    @Expose
    var comment: List<CommentsModel>? = null,
    @SerializedName("totalcount")
    @Expose
    var totalcount: Int? = null,
) : Serializable