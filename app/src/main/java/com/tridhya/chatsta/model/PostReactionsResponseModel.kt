package com.tridhya.chatsta.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostReactionsResponseModel(
    @SerializedName("_id")
    @Expose
    var id: String? = null,
    @SerializedName("imageName")
    @Expose
    var imageName: String? = null,
    @SerializedName("imgUrl")
    @Expose
    var imageUrl: String? = null,
    @SerializedName("mediaType")
    @Expose
    var mediaType: String? = null,
    @SerializedName("gifName")
    @Expose
    var gifName: String? = null,
    @SerializedName("gifUrl")
    @Expose
    var gifUrl: String? = null,
    @SerializedName("isFree")
    @Expose
    var isFree: Boolean? = false,
    @SerializedName("count")
    @Expose
    var count: Long? = 0L,
    @SerializedName("price")
    @Expose
    var price: Double? = 0.0,
    @SerializedName("__v")
    @Expose
    var v: Int? = null,
    @Expose(serialize = false)
    var isSelected: Boolean = false,
) : Serializable