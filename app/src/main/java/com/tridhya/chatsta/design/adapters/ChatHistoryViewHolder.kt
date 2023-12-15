package com.tridhya.chatsta.design.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.ItemMessagesBinding
import com.tridhya.chatsta.enum.chat.MessageType
import com.tridhya.chatsta.enum.chat.UserStatus
import com.tridhya.chatsta.extensions.invisible
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.Conversation
import com.tridhya.chatsta.utils.DateTimeUtils
import com.tridhya.chatsta.utils.GlideUtils

class ChatHistoryViewHolder(var mBinder: ItemMessagesBinding, val context: Context) :
    RecyclerView.ViewHolder(mBinder.root) {
    private var lastSeen: Long = 0
    private var isOnline: UserStatus = UserStatus.OFFLINE
    private var userSetStatus: UserStatus = UserStatus.ONLINE

    fun bind(model: Conversation) {
        if (model.group) {
            mBinder.tvUserName.text = model.groupName
            GlideUtils(mBinder.ivProfileImage.context).circleImage()
                .loadImage(R.drawable.ic_app, mBinder.ivProfileImage)
        } else {
            mBinder.tvUserName.text = model.userName
            if (model.profilePic != null) {
                GlideUtils(mBinder.ivProfileImage.context).circleImage()
                    .loadImage(model.profilePic, mBinder.ivProfileImage)
            } else {
                GlideUtils(mBinder.ivProfileImage.context).circleImage()
                    .loadImage(R.drawable.ic_app, mBinder.ivProfileImage)
            }
        }

        if (userSetStatus == UserStatus.ONLINE) {
//            mBinder.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_blue)
            mBinder.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_green)
        } else {
            when (userSetStatus) {
                UserStatus.ONLINE -> {
                    mBinder.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_green)
                }

                UserStatus.BUSY -> {
                    mBinder.ivProfileIndicator.setImageResource(R.drawable.ic_busy_status)
                }

                else -> {
                    mBinder.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_red)
                }
            }
        }
        if (model.unreadCount!! > 0) {
            mBinder.tvCount.visible()
        } else {
            mBinder.tvCount.invisible()
        }
        mBinder.tvCount.text = model.unreadCount.toString()

        if (model != null) {
            if (model.lastMessage?.type?.equals(MessageType.IMAGE) == true) {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_image_black,
                    0,
                    0,
                    0
                )
                mBinder.tvMessage.text = "Photo"
            } else if (model.lastMessage?.type?.equals(MessageType.VIDEO) == true) {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_videocam_black,
                    0,
                    0,
                    0
                )
                mBinder.tvMessage.text = "Video"
            } else if (model.lastMessage?.type?.equals(MessageType.AUDIO) == true) {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_audio_black,
                    0,
                    0,
                    0
                )
                mBinder.tvMessage.text = "Audio"
            } else if (model.lastMessage?.type?.equals(MessageType.FILE) == true) {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_pdf_black,
                    0,
                    0,
                    0
                )
                mBinder.tvMessage.text = "File"
            } else if (model.lastMessage?.type?.equals(MessageType.EMOJI) == true) {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_chat_emoji_grey_12,
                    0,
                    0,
                    0
                )
                mBinder.tvMessage.text = "Emoji"
            } else if (model.lastMessage?.type?.equals(MessageType.SOUND_EMOJI) == true) {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_chat_emoji_grey_12,
                    0,
                    0,
                    0
                )
                mBinder.tvMessage.text = "Emoji"
            } else if (model.lastMessage?.type?.equals(MessageType.CREATED_GROUP) == true) {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    0
                )
                mBinder.tvMessage.text = "created group"
            } else if (model.lastMessage?.type?.equals(MessageType.ADDED_MEMBERS) == true) {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    0
                )
                mBinder.tvMessage.text = "added " + model.lastMessage?.message
            } else if (model.lastMessage?.type?.equals(MessageType.DELETED_MEMBERS) == true) {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    0
                )
                mBinder.tvMessage.text = "removed " + model.lastMessage?.message
            } else if (model.lastMessage?.type?.equals(MessageType.SCREEN_SHOT) == true) {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_warning_12,
                    0,
                    0,
                    0
                )
                mBinder.tvMessage.text = "Screenshot"
            } else {
                mBinder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    0
                )
                try {
                    mBinder.tvMessage.text = model.lastMessage?.message
                } catch (e: Exception) {
                    mBinder.tvMessage.text = model.lastMessage?.message
                }

            }


            model.lastMessage?.timestamp?.let {
//                val dayTime = TimeStamp.getPastDayTime(it)
                val time = DateTimeUtils.covertTimeStampToTextNew(it.toString())
                mBinder.tvTime.text = String.format("%s", time)
            }
        }
    }
}