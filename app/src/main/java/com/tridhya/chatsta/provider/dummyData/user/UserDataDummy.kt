package com.tridhya.chatsta.provider.dummyData.user

import com.tridhya.chatsta.model.AllInterestResponseModel
import com.tridhya.chatsta.model.Images
import com.tridhya.chatsta.model.User

object UserDataDummy {

    fun getUser1(): User {
        return User(
            "1",
            "atuny0@sohu.com",
            "Terry",
            "Medhurst",
            "Smitham",
            "Test@123",
            "1234",
            "Calefornia,USA",
            "Female",
            "Single",
            "Virgo",
            189,
            intrests,
            null,
            "Test Bio, user details",
            null,
            images,
            null,
            "online",
            false,
            false,
            profilePhotoUrl = "https://source.unsplash.com/user/c_v_r/1900x800"
        )

    }

    val intrests = listOf(
        AllInterestResponseModel("1", "Reading", "10", false),
        AllInterestResponseModel("2", "Dancing", "6", true),
        AllInterestResponseModel("3", "Singing", "6", true),
        AllInterestResponseModel("4", "Listening", "8", false),
        AllInterestResponseModel("5", "Writing", "8", false),
        AllInterestResponseModel("6", "Acting", "8", true),
        AllInterestResponseModel("7", "Modeling", "8", false),
        AllInterestResponseModel("8", "Coding", "8", false),
        AllInterestResponseModel("9", "Teaching", "8", false),
        AllInterestResponseModel("10", "Helping", "10", true),
    )
    val images = listOf(
        Images("https://source.unsplash.com/user/c_v_r/1900x800", "1"),
        Images("https://source.unsplash.com/user/c_v_r/1900x800", "1"),
        Images("https://source.unsplash.com/user/c_v_r/1900x800", "1"),
        Images("https://source.unsplash.com/user/c_v_r/1900x800", "1"),
    )
}