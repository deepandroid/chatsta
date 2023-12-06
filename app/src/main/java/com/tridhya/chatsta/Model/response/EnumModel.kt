package com.tridhya.chatsta.Model.response

import com.google.gson.annotations.Expose
import java.io.Serializable

data class EnumDataModel(
    @Expose
    private var id: Int? = null,
    @Expose
    var text: String,
    @Expose
    var enum: String,
    @Expose
    var imagePath: Int,
    @Expose
    var isSelected: Boolean = false
) : Serializable
