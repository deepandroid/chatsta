package com.tridhya.chatsta.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyConnectionsResponseModel(
    @SerializedName("connections")
    @Expose
    var connections: List<User>? = null,
    @SerializedName("connectionsCount")
    @Expose
    var connectionsCount: Int? = 0,
    @SerializedName("connectionRequestsCount")
    @Expose
    var connectionRequestsCount: Int? = 0,
) : Serializable