package com.tridhya.chatsta.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CommentReplyResponseModel(
    @SerializedName("_id")
    @Expose
    var id: String? = null,
    @SerializedName("comment")
    @Expose
    var repliedComment: String? = null,
    @SerializedName("repliedby")
    @Expose
    var repliedby: User? = null,
    @SerializedName("media")
    @Expose
    var media: String? = null,
    @SerializedName("repliedAt")
    @Expose
    var repliedAt: String? = null,
    @SerializedName("repliedAtTimeStamp")
    @Expose
    var repliedAtTimeStamp: String? = null,
) : Serializable