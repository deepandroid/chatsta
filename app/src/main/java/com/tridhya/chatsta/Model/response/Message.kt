package com.tridhya.chatsta.Model.response

import com.tridhya.chatsta.enum.chat.MediaStatus
import com.tridhya.chatsta.enum.chat.MessageStatus
import com.tridhya.chatsta.enum.chat.MessageType
import com.tridhya.chatsta.provider.Constants
import java.io.Serializable


class Message : Serializable {
    var mediaStatus: MediaStatus = MediaStatus.NOTAPPLICABLE //Default will be Not Available
    var status: MessageStatus = MessageStatus.PENDING //Default will be pending
    var type: MessageType = MessageType.TEXT //Default will be text
    var senderId: String? = null
    var mediaUrl: String? = null
    var audioUrl: String? = null
    var thumbnailUrl: String? = null
    var fileDuration: String? = null
    var message: String? = null
    var id: String? = null //Default will be Not Available
    var selfDestruct: String? = Constants.SELF_DESTRUCT_OFF //Default will be Not Available
    var readBy: HashMap<String, Boolean>? = null
//    var reactions: HashMap<String, ChatReactionsModel>? = null

    var timestamp: Long = System.currentTimeMillis()

    constructor(
        id: String,
        mediaStatus: MediaStatus,
        status: MessageStatus,
        type: MessageType,
        senderId: String?,
        mediaUrl: String?,
        fileDuration: String?,
        message: String?,
    ) {
        this.mediaStatus = mediaStatus
        this.status = status
        this.type = type
        this.senderId = senderId
        this.mediaUrl = mediaUrl
        this.fileDuration = fileDuration
        this.message = message
        this.id = id
    }

    constructor(
        id: String,
        type: MessageType,
        mediaStatus: MediaStatus,
        senderId: String?,
        mediaUrl: String?,
        thumbnailUrl: String?,
        fileDuration: String?,
        timeStamp: Long = System.currentTimeMillis(),
    ) {
        this.type = type
        this.mediaStatus = mediaStatus
        this.senderId = senderId
        this.mediaUrl = mediaUrl
        this.thumbnailUrl = thumbnailUrl
        this.fileDuration = fileDuration
        this.id = id
        this.timestamp = timeStamp
    }

    constructor(
        id: String,
        type: MessageType,
        mediaStatus: MediaStatus,
        senderId: String?,
        mediaUrl: String?,
        thumbnailUrl: String?,
        fileDuration: String?,
        timeStamp: Long = System.currentTimeMillis(),
        selfDestruct: String?,
    ) {
        this.type = type
        this.mediaStatus = mediaStatus
        this.senderId = senderId
        this.mediaUrl = mediaUrl
        this.thumbnailUrl = thumbnailUrl
        this.fileDuration = fileDuration
        this.id = id
        this.timestamp = timeStamp
        this.selfDestruct = selfDestruct
    }

    constructor(
        id: String,
        type: MessageType,
        mediaStatus: MediaStatus,
        senderId: String?,
    ) {
        this.type = type
        this.mediaStatus = mediaStatus
        this.senderId = senderId
        this.id = id
    }

    constructor(id: String, senderId: String?, mediaUrl: String?, message: String?) {
        this.senderId = senderId
        this.mediaUrl = mediaUrl
        this.message = message
        this.id = id
    }

    constructor(id: String, senderId: String?, message: String?) {
        this.senderId = senderId
        this.message = message
        this.id = id
    }

    constructor(
        id: String,
        senderId: String?,
        message: String?,
        selfDestruct: String?,
        mediaUrl: String? = null,
    ) {
        this.senderId = senderId
        this.message = message
        this.id = id
        this.selfDestruct = selfDestruct
    }

    constructor(id: String, senderId: String?, message: String?, timeStamp: Long) {
        this.senderId = senderId
        this.message = message
        this.id = id
        this.timestamp = timeStamp
    }

    constructor(message: String, id: String, senderId: String?, type: MessageType) {
        this.message = message
        this.senderId = senderId
        this.type = type
        this.id = id
    }

    constructor(
        mediaUrl: String,
        audioUrl: String,
        id: String,
        senderId: String?,
        type: MessageType,
    ) {
        this.mediaUrl = mediaUrl
        this.audioUrl = audioUrl
        this.senderId = senderId
        this.type = type
        this.id = id
    }

    constructor(
        mediaUrl: String,
        audioUrl: String,
        id: String,
        senderId: String?,
        type: MessageType,
        selfDestruct: String?,
    ) {
        this.mediaUrl = mediaUrl
        this.audioUrl = audioUrl
        this.senderId = senderId
        this.type = type
        this.id = id
        this.selfDestruct = selfDestruct
    }

    constructor(
        mediaUrl: String,
        audioUrl: String,
        id: String,
        senderId: String?,
        timeStamp: Long,
        type: MessageType,
    ) {
        this.mediaUrl = mediaUrl
        this.audioUrl = audioUrl
        this.senderId = senderId
        this.type = type
        this.timestamp = timeStamp
        this.id = id
    }

    constructor(id: String, senderId: String?, type: MessageType) {
        this.senderId = senderId
        this.type = type
        this.id = id
    }

    constructor()

}