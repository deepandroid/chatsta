package com.tridhya.chatsta.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


data class ChatModel(
    var id: Int = 0,
    var message: String? = null,
    var sender: Int? = null,
    var chatListId: Int? = 0,
    var chatTime: String? = null
)




