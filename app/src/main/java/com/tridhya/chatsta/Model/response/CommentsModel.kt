package com.tridhya.chatsta.Model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tridhya.chatsta.Model.User
import java.io.Serializable

data class CommentsModel(
    @SerializedName("_id")
    @Expose
    var id: String? = null,
    @SerializedName("comment")
    @Expose
    var comment: String? = null,
    @SerializedName("commentCount")
    @Expose
    var commentCount: Int? = 0,
    @SerializedName("postId")
    @Expose
    var postId: String? = null,
    @SerializedName("postedBy")
    @Expose
    var postedBy: String? = null,
    @SerializedName("commentedby")
    @Expose var commentedby: User? = null,
    @SerializedName("media")
    @Expose
    var media: String? = null,
    /*    @SerializedName("replies")
        @Expose
        var replies: List<CommentReplyResponseModel>? = null,*/
    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null,
    @SerializedName("createdTimeStamp")
    @Expose
    var createdTimeStamp: String? = null,
    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null,
    @SerializedName("__v")
    @Expose
    var v: Int? = null,
) : Serializable
