package com.tridhya.chatsta.model

import com.tridhya.chatsta.provider.Constants

class Conversation {
    var id: String? = null
    var group = false
    var groupName: String? = null
    var groupImage: String? = null
    var userId: String? = null
    var lastMessage: Message? = null
    var lastDeleted: Long? = null
    var lastUpdate: Long? = null
    var unreadCount: Long? = 0
    var name: String? = null
    var selfDestruct: String? = Constants.SELF_DESTRUCT_OFF
    private var isChatMuted: Boolean? = false
    var userName: String? = null
    var sendBy: String? = null
    var profilePic: String? = null
    constructor()

    constructor(id: String?, userId: String?, unreadCount: Long? = 0, lastMessage: Message?) {
        this.id = id
        this.userId = userId
        this.unreadCount = unreadCount
        this.lastMessage = lastMessage
    }
    constructor(
        id: String?, userId: String?, unreadCount: Long? = 0, lastMessage: Message?,
        userName:String?,
        profilePic: String?
    ) {
        this.id = id
        this.userId = userId
        this.unreadCount = unreadCount
        this.lastMessage = lastMessage
        this.userName = userName
        this.profilePic = profilePic
    }
    constructor(
        id: String?,
        userId: String?,
        unreadCount: Long? = 0,
        lastMessage: Message?,
        lastDeleted: Long?,
    ) {
        this.id = id
        this.userId = userId
        this.unreadCount = unreadCount
        this.lastMessage = lastMessage
        this.lastDeleted = lastDeleted
    }

    constructor(
        id: String?,
        group: Boolean = true,
        groupName: String?,
        groupImage: String?,
        unreadCount: Long? = 0,
        lastMessage: Message?,
    ) {
        this.id = id
        this.group = group
        this.groupName = groupName
        this.groupImage = groupImage
        this.unreadCount = unreadCount
        this.lastMessage = lastMessage
    }

    constructor(
        id: String?,
        group: Boolean = true,
        groupName: String?,
        groupImage: String?,
        unreadCount: Long? = 0,
        lastMessage: Message?,
        sendBy:String
    ) {
        this.id = id
        this.group = group
        this.groupName = groupName
        this.groupImage = groupImage
        this.unreadCount = unreadCount
        this.lastMessage = lastMessage
        this.sendBy = sendBy
    }
    constructor(
        id: String?,
        group: Boolean = true,
        groupName: String?,
        groupImage: String?,
        unreadCount: Long? = 0,
        lastMessage: Message?,
        selfDestruct: String?,
        lastUpdate: Long?,
    ) {
        this.id = id
        this.group = group
        this.groupName = groupName
        this.groupImage = groupImage
        this.unreadCount = unreadCount
        this.lastMessage = lastMessage
        this.selfDestruct = selfDestruct
        this.lastUpdate = lastUpdate
    }


}
