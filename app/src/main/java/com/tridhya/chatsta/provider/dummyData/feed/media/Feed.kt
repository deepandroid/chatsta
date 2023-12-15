package com.tridhya.chatsta.provider.dummyData.feed.media

import com.tridhya.chatsta.R
import com.tridhya.chatsta.model.PostMediaModel
import com.tridhya.chatsta.model.PostModel
import com.tridhya.chatsta.model.PostReactionsResponseModel
import com.tridhya.chatsta.provider.dummyData.user.UserDataDummy

object Feed {
    fun getFeedPostList(): ArrayList<PostModel> {
        return arrayListOf(
            PostModel(
                "1",
                UserDataDummy.getUser1(),
                postType = "M",
                text = "Amazing photos of Nature, deep dive into nature.",
                medias = mediaOfPost,
                createdAt = System.currentTimeMillis().toString(),
                burnTimestamp = System.currentTimeMillis().toString(),
                reactions = listOfReactions,
                commentCount = 10,
            ),
            PostModel(
                "1",
                UserDataDummy.getUser1(),
                "M",
                "Memories of the time. Uploading image every week, Don't forgot to subscribe and connect!",
                medias = mediaOfPost2,
                createdAt = System.currentTimeMillis().toString(),
                reactions = listOfReactions,
                burnTimestamp = System.currentTimeMillis().toString(),
                commentCount = 5,
            ),
            PostModel(
                "1",
                UserDataDummy.getUser1(),
                "image",
                "Post",
                mediaOfPost,
                System.currentTimeMillis().toString(),
                burnTimestamp = System.currentTimeMillis().toString(),
                reactions = listOfReactions
            ),
            PostModel(
                "1",
                UserDataDummy.getUser1(),
                "image",
                "Post",
                mediaOfPost,
                System.currentTimeMillis().toString(),
                burnTimestamp = System.currentTimeMillis().toString(),
                reactions = listOfReactions,
            ),
            PostModel(
                "1",
                UserDataDummy.getUser1(),
                "image",
                "Post",
                mediaOfPost,
                System.currentTimeMillis().toString(),
                burnTimestamp = System.currentTimeMillis().toString(),
                reactions = listOfReactions
            )
        )
    }

    val mediaOfPost = listOf(
        PostMediaModel("I", url = "https://source.unsplash.com/random/1900x800"),
        PostMediaModel("I", url = "https://source.unsplash.com/random/1900x800?sig=1"),
        PostMediaModel("I", url = "https://source.unsplash.com/random/1900x800?sig=2")
    )
    val mediaOfPost2 = listOf(
        PostMediaModel("I", url = "https://source.unsplash.com/random/1900x800?sig=3"),
        PostMediaModel("I", url = "https://source.unsplash.com/random/1900x800?sig=4")
    )

    val listOfReactions = listOf(
        PostReactionsResponseModel(
            emojiUrl = R.drawable.emoji_reaction5,
            isFree = true,
            id = "1",
            gifDrawable = R.drawable.heart_confetti
        ),
        PostReactionsResponseModel(
            emojiUrl = R.drawable.emoji_reaction4,
            isFree = true,
            id = "2",
            gifDrawable = R.drawable.poo_confetti
        ),
        PostReactionsResponseModel(
            emojiUrl = R.drawable.emoji_reaction3,
            isFree = false,
            id = "3",
            price = 0.5,
            gifDrawable = R.drawable.rose_confetti
        ),
        PostReactionsResponseModel(
            emojiUrl = R.drawable.emoji_reaction2,
            isFree = false,
            id = "4",
            price = 1.0,
            gifDrawable = R.drawable.coin_confetti
        ),
        PostReactionsResponseModel(
            emojiUrl = R.drawable.emoji_reaction1,
            isFree = false,
            id = "5",
            price = 1.5,
            gifDrawable = R.drawable.bill_confetti
        ),
    )
}