package com.tridhya.chatsta.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("_id")
    @Expose
    var userId: String? = null,
    @SerializedName("email")
    @Expose
    var email: String? = null,
    @SerializedName("firstName")
    @Expose
    var firstName: String? = null,
    @SerializedName("lastName")
    @Expose
    var lastName: String? = null,
    @SerializedName("username")
    @Expose
    var username: String? = null,
    @SerializedName("password")
    @Expose
    var password: String? = null,
    @SerializedName("pin")
    @Expose
    var pin: String? = null,
    @SerializedName("location")
    @Expose
    var location: String? = null,
    @SerializedName("gender")
    @Expose
    var gender: String? = null,
    @SerializedName("relationShipStatus")
    @Expose
    var relationShipStatus: String? = null,
    @SerializedName("starSign")
    @Expose
    var starSign: String? = null,
    @SerializedName("height")
    @Expose
    var height: Int? = null,
    @SerializedName("interests")
    @Expose
    var interests: List<AllInterestResponseModel>? = null,
    @SerializedName("posts")
    @Expose
    var posts: List<Any?>? = null,
    @SerializedName("bio")
    @Expose
    var bio: String? = null,
    @SerializedName("quotes")
    @Expose
    var quotes: String? = null,
    @SerializedName("images")
    @Expose
    var images: List<Images>? = null,
    @SerializedName("role")
    @Expose
    var role: String? = null,
    @SerializedName("status")
    @Expose
    var status: String? = null,
    @SerializedName("isBlocked")
    @Expose
    var isBlocked: Boolean? = null,
    @SerializedName("isPaidContentProvider")
    @Expose
    var isPaidContentProvider: Boolean? = null,
    @SerializedName("stripeKey")
    @Expose
    var stripeKey: String? = null,
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
    var firebaseToken: String? = null,
    @SerializedName("deviceType")
    @Expose
    var deviceType: String? = null,
    @SerializedName("totalDonated")
    @Expose
    var totalDonated: Double? = 0.0,
    @SerializedName("profilePhotoUrl")
    @Expose
    var profilePhotoUrl: String? = null,
    @SerializedName("registerDate")
    @Expose
    var registerDate: String? = null,
    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null,
    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null,
    @SerializedName("existingContent")
    @Expose
    private var existingContent: String? = null,
    @SerializedName("contentType")
    @Expose
    private var contentType: String? = null,
    @SerializedName("tiktokLink")
    @Expose
    private var tiktokLink: String? = null,
    @SerializedName("instagramLink")
    @Expose
    private var instagramLink: String? = null,
    @SerializedName("youtubeLink")
    @Expose
    private var youtubeLink: String? = null,
    @SerializedName("facebookLink")
    @Expose
    private var facebookLink: String? = null,
    @SerializedName("__v")
    @Expose
    var v: Int? = null,
    @SerializedName("freeReaction")
    @Expose
    var freeReaction: Int? = null,
    @SerializedName("totalDue")
    @Expose
    var totalDue: Double? = 0.0,
    @SerializedName("totalIncome")
    @Expose
    var totalIncome: Double? = 0.0,
    @SerializedName("isMember")
    @Expose
    var isMember: Boolean? = false,
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