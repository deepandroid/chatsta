package com.tridhya.chatsta.Model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tridhya.chatsta.Model.User
import java.io.Serializable

data class PostModel(
    @SerializedName("_id")
    @Expose
    var id: String? = null,
    @SerializedName("postedBy")
    @Expose
    var postedBy: User? = null,
    @SerializedName("postType")
    @Expose
    var postType: String? = null,
    @SerializedName("text")
    @Expose
    var text: String? = null,
    /* @SerializedName("medias")
     @Expose
     var medias: List<PostMediaModel> = arrayListOf(),*/
    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null,
    @SerializedName("totalDonation")
    @Expose
    var totalDonation: Double? = 0.0,
    /*@SerializedName("reaction")
    @Expose
    var reactions: List<PostReactionsResponseModel> = arrayListOf(),*/
    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null,
    @SerializedName("commentCount")
    @Expose
    var commentCount: Int? = 0,
    @SerializedName("burnTimestamp")
    @Expose
    var burnTimestamp: String? = null,
    @SerializedName("burnTimestamp48hours")
    @Expose
    var burnTimestamp48hours: String? = null,
    @SerializedName("__v")
    @Expose
    var v: Int? = null,
) : Serializable
