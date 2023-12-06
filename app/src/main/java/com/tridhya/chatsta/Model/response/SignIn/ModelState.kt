package com.tridhya.chatsta.Model.response.SignIn


import com.google.gson.annotations.SerializedName

data class ModelState(
    @SerializedName("log.email")
    val logEmail: List<String>,
)