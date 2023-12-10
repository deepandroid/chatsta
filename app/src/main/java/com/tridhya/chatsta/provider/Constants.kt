package com.tridhya.chatsta.provider

import com.tridhya.chatsta.model.User

object Constants {

    //Places key
    const val GOOGLE_MAP_KEYS = "AIzaSyC937CkLN5X7nOW0bmv5aq71x97G6zohkU"
    const val PLACE_PICKER_REQUEST = 1000

    //AWS
    const val AWS_SECRET_KEY = "oSGUCThx8hhcDD3KfHzuUtJ/UDdYiVF+DZmKKLf0"
    const val AWS_ACCESS_KEY = "AKIAX2UCSFS3EJISEK4E"
    const val AWS_BUCKET_NAME = "chatsta-media"
    const val AWS_BUCKET_REGION = "ap-south-1"
    const val BUCKET_POOL_ID =
        "ap-south-1:d9c986fc-4436-491c-8a69-1f37c7e98fc1" // set your cognito pool id.

    //AES
    const val AES_IV = "5183666c72eec9e4"
    const val AES_KEY = "bf3c199c2470cb477d907b1e0917c17b"

    const val PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[0-9]).{6,}$"
    const val SESSION_EXPIRE_MSG = "Session Expired"
    const val UNAUTHORIZED = "Unauthorized User"
    const val ILLEGAL_STATE_EXCEPTION = "IllegalStateException"

    //Gender
    const val MALE = "male"
    const val FEMALE = "female"
    const val NON_BINARY = "nonbinary"

    //Relationship Status
    const val SINGLE = "single"
    const val IN_A_RELATIONSHIP = "inarelationship"
    const val ENGAGED = "engaged"
    const val MARRIED = "married"
    const val IN_LOVE = "inlove"
    const val ACTIVELY_SEARCHING = "activelysearching"
    const val IN_A_CIVIL_UNION = "inacivilunion"
    const val ITS_COMPLICATED = "itscomplicated"
    const val DIVORCED = "divorced"

    //Star Sign
    const val ARIES = "aries"
    const val TAURUS = "taurus"
    const val GEMINI = "gemini"
    const val CANCER = "cancer"
    const val LEO = "leo"
    const val VIRGO = "virgo"
    const val LIBRA = "libra"
    const val SCORPIO = "scorpio"
    const val SAGITTARIUS = "sagittarius"
    const val CAPRICORN = "capricorn"
    const val AQUARIUS = "aquarius"
    const val PISCES = "pisces"

    var isShowed = false
    const val DATA = "data"
    const val TAG = "tag"
    const val OLD_PIN = "oldPin"
    const val NEW_PIN = "newPin"

    //CMS Pages
    const val VERSION_INFO = "version"

    //MediaType
    const val IMAGE = "I"
    const val VIDEO = "V"
    const val AUDIO = "A"
    const val TEXT = "T"
    const val MEDIA = "M"
    const val FILE = "F"

    //Filter Feed
    const val MOST_POPULAR = "popular"
    const val MOST_RECENT = "recent"
    const val POST_PAGE_SIZE = 10
    const val CONNECTIONS_PAGE_SIZE = 20
    const val COMMENTS_PAGE_SIZE = 10
    const val NOTIFICATION_PAGE_SIZE = 20
    const val DONATION_HISTORY_PAGE_SIZE = 20
    const val TYPE_FEED = "feed"
    const val TYPE_PUBLIC = "public"

    //Connections Request
    const val DEFAULT = 0
    const val ACCEPTED = 1
    const val DECLINED = 2

    //Connections
    const val DELETE_ALL_REQUESTS = "all"
    const val CONNECTED = 1
    const val ALREADY_REQUESTED = 2
    const val NORMAL_USER = 3
    const val REQUEST_RECEIVED = 4

    const val BLOCK_USER = 1
    const val UNBLOCK_USER = 2

    //Chat
    const val DB_NAME = "Chatsta"
    const val TABLE_CHAT = "Chat"
    const val TABLE_USERS = "Users"
    const val TABLE_GROUPS = "Groups"
    const val TABLE_THREADS = "Threads"
    const val TABLE_MESSAGES = "Messages"
    const val TABLE_SCHEDULE = "ScheduledMessages"

//    const val TABLE_CONVERSATIONS = "Conversations_TEST"

    const val TABLE_CONVERSATIONS = "Conversations"
    const val NOTIFICATION_TYPE = "notificationType"
    const val FIREBASE_TOKEN = "firebaseToken"

    //Paid Content Provider
    var IS_PAID_CONTENT_PROVIDER = false

    //reporttypes
    const val COMMENTS_TYPE = "comment"

    //notification
    const val POST_ID = "postId"
    const val NOTIFICATION_ID = "notificationId"
    const val COMMENT_ID = "commentId"
    const val USER_ID = "userId"
    const val REACTION_ID = "reactionId"
    const val CHAT_ID = "chatId"
    const val GROUP_ID = "groupId"

    //notification type
    const val COMMENT_TYPE = "comment_type"
    const val REACTION_TYPE = "reaction_type"
    const val CHAT_DONATION_TYPE = "chat_donation_type"
    const val POST_DONATION_TYPE = "post_donation_type"
    const val REQUEST_TYPE = "request_type"
    const val CHAT_TYPE = "chat_type"
    const val GROUP_TYPE = "group_type"
    const val OTHER_DEVICE_SIGNIN = "singleLogin_type"
    const val FIREBASE_CHAT = "firebase_chat"
    const val FIREBASE_GROUP_CHAT = "firebase_group_chat"

    //audio
    const val RECORD_AUDIO = 1

    //payment type
    const val AMOUNT_DUE = "amount_due"
    const val INCOME = "income"

    //stripe
    const val STRIPE_SECRET_KEY =
        "sk_test_51KiTwbHUnpsFl2LGmYaca8qHMUmhgJLxddZaZncCBGa3KQto4voBOKfUafZIDwO7iPJgR6gmeMrhmg4NjiUrc5JC00HeYc680I"
    const val STRIPE_PUBLISHABLE_KEY =
        "pk_test_51KiTwbHUnpsFl2LGU2xRz3TEtzgECaijTyJ48f7tUPz44jedBPLfstO1DtnG6zPxPkEiima0UB6pMCjl3ZMgs44000Ioykgn1d"
    const val PAYMENT_COMPLETED = "Completed"
    const val PAYMENT_CANCELED = "Canceled"
    const val PAYMENT_FAILED = "Failed"

    //self destruct message
    const val SELF_DESTRUCT_5_MIN = "5min."
    const val SELF_DESTRUCT_1_HOUR = "1h"
    const val SELF_DESTRUCT_1_WEEK = "1w"
    const val SELF_DESTRUCT_OFF = "off"

    val user1 = User(
        "1",
        "jessica@gmail.com",
        "Jessica",
        "Franco",
        "Jessica",
        "Test@1234",
        "1234",
        "Gota,Ahmedabad",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "Karma",
        null,
        null,
        null,
        false,
        null,
        null,
        false,
        false,
        false,
        false,
        true,
        false,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        false,
        "",
        3,
        1,
        false,
        0,
        false
    )
    val user2 = User(
        "2",
        "sss@gmail.com",
        "Bbc",
        "xyz",
        "lara",
        "12345",
        "1234",
        "Gota,Ahmedabad",
        "Female",
        "Married",
        "Virgo",
        null,
        null,
        null,
        null,
        "Karma",
        null,
        null,
        null,
        false,
        null,
        null,
        false,
        false,
        false,
        false,
        true,
        false,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        false,
        "",
        3,
        1,
        false,
        0,
        false
    )
    val user3 = User(
        "3",
        "www@gmail.com",
        "Cbc",
        "xyz",
        "lara",
        "12345",
        "1234",
        "Gota,Ahmedabad",
        "Female",
        "Married",
        "Virgo",
        null,
        null,
        null,
        null,
        "Karma",
        null,
        null,
        null,
        false,
        null,
        null,
        false,
        false,
        false,
        false,
        true,
        false,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        false,
        "",
        3,
        1,
        false,
        0,
        false
    )


}