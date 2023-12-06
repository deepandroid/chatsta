package com.tridhya.chatsta.Model.request


import com.google.gson.annotations.SerializedName

data class RegisterationRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String,
)