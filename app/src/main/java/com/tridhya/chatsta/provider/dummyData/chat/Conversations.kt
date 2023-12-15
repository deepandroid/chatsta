package com.tridhya.chatsta.provider.dummyData.chat

import com.tridhya.chatsta.enum.chat.MediaStatus
import com.tridhya.chatsta.enum.chat.MessageType
import com.tridhya.chatsta.model.Conversation
import com.tridhya.chatsta.model.Message

object Conversations {
    fun getConversations(): ArrayList<Conversation> {
        return arrayListOf(
            Conversation(
                "1",
                "101",
                0,
                message,
                "Britt Hoots",
                "https://source.unsplash.com/user/c_v_r/1900x800"
            ),
            Conversation(
                "2",
                "102",
                0,
                message2,
                "Kevin",
                "https://source.unsplash.com/user/c_v_r/1900x800"
            ),
            Conversation(
                "3",
                "103",
                15,
                message3,
                "Rossi Mozelle",
                "https://source.unsplash.com/user/c_v_r/1900x800"
            ),
            Conversation(
                "4",
                "104",
                2,
                message4,
                "Jessica",
                null
            ),

            )
    }

    fun getGroupConversations(): ArrayList<Conversation> {
        return arrayListOf(
            Conversation("1", true, "HangOut", "https://source.unsplash.com/user/c_v_r/1900x800", 2, messageGroup,"jessca:"),
            Conversation("2", true, "Friends Forever", "", 2, message2,"rosan:"),

            )
    }

    val message = Message(id = "1", senderId = "101", message = "I am free at 1PM tomorrow!")
    val messageGroup = Message(id = "1", senderId = "101", message = "Rajiv: I am free at 1PM tomorrow!")
    val message2 = Message(
        id = "2",
        type = MessageType.IMAGE,
        mediaStatus = MediaStatus.SUCCESS,
        senderId = "102"
    )
    val message3 =
        Message(
            id = "3",
            senderId = null,
            selfDestruct = "",
            message = "Hey! What's up, Long time no see?"
        )
    val message4 = Message(id = "1", senderId = "101", message = "Good Morning!")


}