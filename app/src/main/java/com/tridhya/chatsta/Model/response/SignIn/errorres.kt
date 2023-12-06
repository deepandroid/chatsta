package com.tridhya.chatsta.Model.response.SignIn


import com.google.gson.annotations.SerializedName

data class errorres(
    @SerializedName("Message")
    val message: String,
    @SerializedName("ModelState")
    val modelState: ModelState,
)