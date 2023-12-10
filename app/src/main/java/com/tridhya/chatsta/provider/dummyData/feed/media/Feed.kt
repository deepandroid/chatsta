package com.tridhya.chatsta.provider.dummyData.feed.media

import com.tridhya.chatsta.R
import com.tridhya.chatsta.model.PostMediaModel
import com.tridhya.chatsta.model.PostModel
import com.tridhya.chatsta.model.PostReactionsResponseModel
import com.tridhya.chatsta.provider.dummyData.user.UserDataDummy

object Feed {
    fun getFeedPostList(): List<PostModel> {
        return listOf(
            PostModel(
                "1",
                UserDataDummy.getUser1(),
                "image",
                "Post",
                mediaOfPost,
                System.currentTimeMillis().toString(),
                reactions = listOfReactions
            ),
            PostModel(
                "1",
                UserDataDummy.getUser1(),
                "image",
                "Post",
                mediaOfPost,
                System.currentTimeMillis().toString(),
                reactions = listOfReactions
            ),
            PostModel(
                "1",
                UserDataDummy.getUser1(),
                "image",
                "Post",
                mediaOfPost,
                System.currentTimeMillis().toString(),
                reactions = listOfReactions
            ),
            PostModel(
                "1",
                UserDataDummy.getUser1(),
                "image",
                "Post",
                mediaOfPost,
                System.currentTimeMillis().toString(),
                reactions = listOfReactions
            ),
            PostModel(
                "1",
                UserDataDummy.getUser1(),
                "image",
                "Post",
                mediaOfPost,
                System.currentTimeMillis().toString(),
                reactions = listOfReactions
            )
        )
    }

    val mediaOfPost = listOf(
        PostMediaModel("image", "https://source.unsplash.com/user/c_v_r/1900x800"),
        PostMediaModel("image", "https://source.unsplash.com/user/c_v_r/1900x800"),
        PostMediaModel("image", "https://source.unsplash.com/user/c_v_r/1900x800")
    )

    val listOfReactions = listOf(
        PostReactionsResponseModel(emojiUrl = R.drawable.emoji_reaction5, isFree = true, id = "1"),
        PostReactionsResponseModel(emojiUrl = R.drawable.emoji_reaction4, isFree = true, id = "2"),
        PostReactionsResponseModel(
            emojiUrl = R.drawable.emoji_reaction3,
            isFree = false,
            id = "3",
            price = 0.5
        ),
        PostReactionsResponseModel(
            emojiUrl = R.drawable.emoji_reaction2,
            isFree = false,
            id = "4",
            price = 1.0
        ),
        PostReactionsResponseModel(
            emojiUrl = R.drawable.emoji_reaction1,
            isFree = false,
            id = "5",
            price = 1.5
        ),
    )
}