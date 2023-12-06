package com.tridhya.chatsta.Model

import com.tridhya.chatsta.Model.response.AllInterestResponseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("_id")
    @Expose
    val userId: String? = null,
    @SerializedName("email")
    @Expose
    val email: String? = null,
    @SerializedName("firstName")
    @Expose
    val firstName: String? = null,
    @SerializedName("lastName")
    @Expose
    val lastName: String? = null,
    @SerializedName("username")
    @Expose
    val username: String? = null,
    @SerializedName("password")
    @Expose
    val password: String? = null,
    @SerializedName("pin")
    @Expose
    val pin: String? = null,
    @SerializedName("location")
    @Expose
    val location: String? = null,
    @SerializedName("gender")
    @Expose
    val gender: String? = null,
    @SerializedName("relationShipStatus")
    @Expose
    val relationShipStatus: String? = null,
    @SerializedName("starSign")
    @Expose
    val starSign: String? = null,
    @SerializedName("height")
    @Expose
    val height: Int? = null,
    @SerializedName("interests")
    @Expose
    val interests: List<AllInterestResponseModel>? = null,
    @SerializedName("posts")
    @Expose
    val posts: List<Any?>? = null,
    @SerializedName("bio")
    @Expose
    val bio: String? = null,
    @SerializedName("quotes")
    @Expose
    val quotes: String? = null,
    @SerializedName("images")
    @Expose
    val images: List<Images>? = null,
    @SerializedName("role")
    @Expose
    val role: String? = null,
    @SerializedName("status")
    @Expose
    val status: String? = null,
    @SerializedName("isBlocked")
    @Expose
    val isBlocked: Boolean? = null,
    @SerializedName("isPaidContentProvider")
    @Expose
    val isPaidContentProvider: Boolean? = null,
    @SerializedName("stripeKey")
    @Expose
    val stripeKey: String? = null,
    @SerializedName("isPrivateAccount")
    @Expose
    var isAccount: Boolean? = false,
    @SerializedName("messageAlerts")
    @Expose
    var messageAlerts: Boolean? = false,
    @SerializedName("vibration")
    @Expose
    var vibration: Boolean? = false,
    @SerializedName("messagePreview")
    @Expose
    var messagePreview: Boolean? = false,
    @SerializedName("likes")
    @Expose
    var likes: Boolean? = false,
    @SerializedName("comments")
    @Expose
    var comments: Boolean? = false,
    @SerializedName("firebaseToken")
    @Expose
    val firebaseToken: String? = null,
    @SerializedName("deviceType")
    @Expose
    val deviceType: String? = null,
    @SerializedName("totalDonated")
    @Expose
    val totalDonated: Double? = 0.0,
    @SerializedName("profilePhotoUrl")
    @Expose
    val profilePhotoUrl: String? = null,
    @SerializedName("registerDate")
    @Expose
    val registerDate: String? = null,
    @SerializedName("createdAt")
    @Expose
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    @Expose
    val updatedAt: String? = null,
    @SerializedName("existingContent")
    @Expose
    private val existingContent: String? = null,
    @SerializedName("contentType")
    @Expose
    private val contentType: String? = null,
    @SerializedName("tiktokLink")
    @Expose
    private val tiktokLink: String? = null,
    @SerializedName("instagramLink")
    @Expose
    private val instagramLink: String? = null,
    @SerializedName("youtubeLink")
    @Expose
    private val youtubeLink: String? = null,
    @SerializedName("facebookLink")
    @Expose
    private val facebookLink: String? = null,
    @SerializedName("__v")
    @Expose
    val v: Int? = null,
    @SerializedName("freeReaction")
    @Expose
    var freeReaction: Int? = null,
    @SerializedName("totalDue")
    @Expose
    val totalDue: Double? = 0.0,
    @SerializedName("totalIncome")
    @Expose
    val totalIncome: Double? = 0.0,
    @SerializedName("isMember")
    @Expose
    val isMember: Boolean? = false,
    @SerializedName("token")
    var token: String = "",
    @SerializedName("connectionStatus")
    var userConnectionStatus: Int? = 3,
    @SerializedName("isBlockedConnection")
    var isBlockedConnection: Int? = 2,
    @SerializedName("isBlockedForChat")
    var isBlockedForChat: Boolean? = false,
    var connectionRequestStatus: Int = 0,
    var isSelected: Boolean = false,
) : Serializable