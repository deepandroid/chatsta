package com.tridhya.chatsta.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostResponseModel(
    @SerializedName("posts")
    @Expose
    var postList: List<PostModel> = arrayListOf(),
) : Serializable