package com.tridhya.chatsta.provider.dummyData.chat

import com.tridhya.chatsta.model.ChatModel

object getMessages {
    fun getChatMessages(): ArrayList<ChatModel> {
        return arrayListOf(
            ChatModel(1, "Hello", 0, 1, System.currentTimeMillis().toString()),
            ChatModel(1, "Hi", 1, 1, System.currentTimeMillis().toString()),
            ChatModel(1, "How are you?", 0, 1, System.currentTimeMillis().toString()),
            ChatModel(1, "I am Fine", 1, 1, System.currentTimeMillis().toString()),
            ChatModel(1, "How's u?", 1, 1, System.currentTimeMillis().toString()),
            ChatModel(1, "good", 0, 1, System.currentTimeMillis().toString()),
            ChatModel(1, "okay", 1, 1, System.currentTimeMillis().toString()),
            ChatModel(
                1,
                "Motivation is the word derived from the word 'motive' which means needs, desires, wants or drives within the individuals. It is the process of stimulating people to actions to accomplish the goals. In the work goal context the psychological factors stimulating the people's behaviour can be - desire for money.",
                0,
                1,
                System.currentTimeMillis().toString()
            ),
            ChatModel(
                1,
                "ntrinsic and extrinsic motivation are the two main types of motivation and represent all motivational drivers.",
                1,
                1,
                System.currentTimeMillis().toString()
            ),
            ChatModel(1, "can we meet on firday?", 0, 1, System.currentTimeMillis().toString()),
            ChatModel(
                1,
                "What time you want to meet?",
                1,
                1,
                System.currentTimeMillis().toString()
            ),

            ChatModel(1, "At evening?", 0, 1, System.currentTimeMillis().toString()),
            ChatModel(1, "Sounds Good.", 1, 1, System.currentTimeMillis().toString()),
            ChatModel(0, "Hello", 0, 1, System.currentTimeMillis().toString()),
        )
    }

}