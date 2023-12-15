package com.tridhya.chatsta.design.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.DialogChatMessageOptionsBinding
import com.tridhya.chatsta.databinding.DialogChatMessageReactionsBinding
import com.tridhya.chatsta.databinding.DialogSelfDestructMessageOptionsBinding
import com.tridhya.chatsta.databinding.ItemChatReceiverBinding
import com.tridhya.chatsta.databinding.ItemChatSenderBinding
import com.tridhya.chatsta.design.fragments.chat.ChatInfoViewHolder
import com.tridhya.chatsta.enum.chat.MessageStatus
import com.tridhya.chatsta.enum.chat.MessageType
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.invisible
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.interfaces.chat.OnChatItemClick
import com.tridhya.chatsta.model.ChatModel
import com.tridhya.chatsta.model.Message
import com.tridhya.chatsta.model.PostReactionsResponseModel
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.Session

class ChatAdapter(
    private val chatMessages: ArrayList<ChatModel>?,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), OnChatItemClick {

    private val TYPE_SENDER = 1
    private val TYPE_RECEIVER = 2
    var session: Session? = null
    private val currentMediaPlaying: MutableLiveData<Int?> = MutableLiveData(null)


    override fun getItemViewType(position: Int): Int {
        val message = chatMessages?.get(position)
        return if (message?.sender == 0) {
            TYPE_SENDER
        } else {
            TYPE_RECEIVER
        }
    }

    override fun getItemCount(): Int {
        return chatMessages?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_RECEIVER -> {
                val view: ItemChatReceiverBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_chat_receiver, parent, false
                    )
                ChatReceiverTextViewHolder(view, parent.context)
            }

            TYPE_SENDER -> {
                val view: ItemChatSenderBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_chat_sender, parent, false
                    )
                ChatSenderTextViewHolder(parent.context, view, this)
            }

            else -> {
                val view: ItemChatSenderBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_chat_sender, parent, false
                    )
                ChatSenderTextViewHolder(parent.context, view, this)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val message = chatMessages?.get(position)

        when (viewHolder) {
            is ChatSenderTextViewHolder -> {
                viewHolder.bind(message!!)
                viewHolder.binder.container.background =
                    ContextCompat.getDrawable(
                        viewHolder.context,
                        R.drawable.shape_chat_sender
                    )
            }

            is ChatReceiverTextViewHolder -> {
                viewHolder.bind(message!!)
                viewHolder.binder.container.background =
                    ContextCompat.getDrawable(
                        viewHolder.context,
                        R.drawable.shape_chat_receiver
                    )

            }

            is ChatInfoViewHolder -> {
                /*viewHolder.bind(model, session?.user, chatUserId)
                if (lastTime != dayTime) {
                    viewHolder.binder.tvDate.visible()
                    viewHolder.binder.tvDate.text = dayTime?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }
                } else {
                    viewHolder.binder.tvDate.gone()
                }*/
            }
        }
    }

    private fun openChatReactions(view: View, model: Message, context: Context) {
        val popupBinding = DialogChatMessageReactionsBinding.inflate(LayoutInflater.from(context))
        val popup = PopupWindow(
            popupBinding.root,
            720,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popup.animationStyle = com.chaos.view.R.style.Animation_AppCompat_Dialog
        popupBinding.rvReactions.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        popup.showAsDropDown(view, 0, -100, Gravity.TOP)
    }

    private fun updateMessage(msgId: String?) {
        val params: MutableMap<String, Any> = HashMap()
        params["status"] = MessageStatus.READ
//        mThreadReference?.child(msgId.toString())?.updateChildren(params)
    }

    override fun onClick(data: Message) {

    }

    override fun onBurnListener(data: Message) {
//        mThreadReference?.child(data.id.toString())?.removeValue()
        checkLastMessage(data.id.toString())
        if (data.status != MessageStatus.READ) {
            decreaseUnreadCount()
        }
    }

    override fun onLongClick(
        data: Any,
        context: Context,
        root: View,
        imageView: AppCompatImageView,
    ) {
        data as ChatModel
        val popupBinding = DialogChatMessageOptionsBinding.inflate(LayoutInflater.from(context))
        val popup = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val values = IntArray(2)
        root.getLocationInWindow(values)
        val positionOfIcon = values[1]
        println("Position Y:$positionOfIcon")
        val displayMetrics = context.resources.displayMetrics
        val height = displayMetrics.heightPixels * 2 / 3
        println("Height:$height")
        popup.animationStyle = com.chaos.view.R.style.Animation_AppCompat_Dialog
        if (positionOfIcon > height) {
            popup.showAsDropDown(root, 0, -520, Gravity.RIGHT)
        } else {
            popup.showAsDropDown(root, 0, 0, Gravity.RIGHT)
        }

        popupBinding.llCopyMain.isVisible == true

        popupBinding.llWipe.setOnClickListener {
            popup.dismiss()
            imageView.visible()
            GlideUtils(context).loadGif(R.raw.wipe_animation, imageView)
            Handler(Looper.getMainLooper()).postDelayed({
                imageView.gone()
//                mThreadReference?.child(data.id.toString())?.removeValue()
                checkLastMessage(data.id.toString())
               /* if (data.status != MessageStatus.READ) {
                    decreaseUnreadCount()
                }*/
            }, 1500)
        }

        popupBinding.llCopy.setOnClickListener {
            popup.dismiss()
            val message =
                data.message.toString()
            val clipboard: ClipboardManager? =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", message)
            clipboard?.setPrimaryClip(clip)
        }

        popupBinding.llSelfDestructTime.setOnClickListener {
            popup.dismiss()
            showPopUpMenu(root, data, context)
        }
    }

    private fun showPopUpMenu(it: View?, data: ChatModel, context: Context) {
        val dialogBinding =
            DialogSelfDestructMessageOptionsBinding.inflate(LayoutInflater.from(context))
        val popUp = PopupWindow(
            dialogBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val values2 = IntArray(2)
        it!!.getLocationInWindow(values2)
        val positionOfIcon2 = values2[1]
        println("Position Y:$positionOfIcon2")
        val displayMetrics2 = context.resources.displayMetrics
        val height2 = displayMetrics2.heightPixels * 2 / 3
        println("Height:$height2")
        popUp.animationStyle = com.chaos.view.R.style.Animation_AppCompat_Dialog
        if (positionOfIcon2 > height2) {
            popUp.showAsDropDown(it, 0, -520, Gravity.RIGHT)
        } else {
            popUp.showAsDropDown(it, 0, 0, Gravity.RIGHT)
        }
//        popUp.animationStyle = com.chaos.view.R.style.Animation_AppCompat_Dialog
//        popUp.showAsDropDown(it, 0, 0, Gravity.TOP)
        when (Constants.SELF_DESTRUCT_OFF) {
            Constants.SELF_DESTRUCT_OFF -> {
                dialogBinding.tv4.isSelected = true
            }

            Constants.SELF_DESTRUCT_5_MIN -> {
                dialogBinding.tv1.isSelected = true
            }

            Constants.SELF_DESTRUCT_1_HOUR -> {
                dialogBinding.tv2.isSelected = true
            }

            Constants.SELF_DESTRUCT_1_WEEK -> {
                dialogBinding.tv3.isSelected = true
            }
        }
        dialogBinding.tv1.setOnClickListener {
            dialogBinding.tv1.isSelected = true
            updateSelfDestructTime(data.id.toString(), dialogBinding.tv1.text.toString())
            popUp.dismiss()
        }
        dialogBinding.tv2.setOnClickListener {
            dialogBinding.tv2.isSelected = true
            updateSelfDestructTime(data.id.toString(), dialogBinding.tv2.text.toString())
            popUp.dismiss()
        }
        dialogBinding.tv3.setOnClickListener {
            dialogBinding.tv3.isSelected = true
            updateSelfDestructTime(data.id.toString(), dialogBinding.tv3.text.toString())
            popUp.dismiss()
        }
        dialogBinding.tv4.setOnClickListener {
            dialogBinding.tv4.isSelected = true
            updateSelfDestructTime(data.id.toString(), dialogBinding.tv4.text.toString())
            popUp.dismiss()
        }

    }

    private fun updateSelfDestructTime(msgId: String?, destructTime: String?) {
        val params: MutableMap<String, Any> = HashMap()
        params["selfDestruct"] = destructTime.toString()
//        mThreadReference?.child(msgId.toString())?.updateChildren(params)
    }

    private fun checkLastMessage(messageId: String) {
        /* mDBConversations.child(session?.user?.userId.toString()).child(threadRef)
             .child("lastMessage")
             .addListenerForSingleValueEvent(
                 object : ValueEventListener {
                     override fun onDataChange(dataSnapshot: DataSnapshot) {
                         if (dataSnapshot.exists()) {
                             val values = dataSnapshot.value as HashMap<*, *>?
                             if (values != null && values["id"].toString() == messageId) {
                                 if (itemCount <= 0) {
                                     removeLastMessage()
                                 } else {
                                     updateLastMessage(snapshots[itemCount - 1])
                                 }
                             }
                         }
                     }

                     override fun onCancelled(error: DatabaseError) {}

                 })*/
    }

    private fun removeLastMessage() {
        /*mDBConversations.child(session?.user?.userId.toString())
            .child(threadRef)
            .child("lastMessage").removeValue()
        mDBConversations.child(chatUserId.toString()).child(threadRef)
            .child("lastMessage").removeValue()*/
    }

    private fun decreaseUnreadCount() {
        /*        mDBConversations.child(chatUserId.toString())
                    .child(threadRef).child("unreadCount")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(ds: DataSnapshot) {
                            var count: Long = 0

                            if (ds.value != null) {
                                count = ds.value as Long
                            }
                            if (count > 0) {
                                val params: MutableMap<String, Any> = HashMap()
                                params["unreadCount"] = count - 1
                                mDBConversations.child(chatUserId.toString()).child(threadRef)
                                    .updateChildren(params)
                            }

                        }
                    })*/
    }

    private fun updateLastMessage(message: Message) {
        val params: MutableMap<String, Any> = HashMap()
        params["lastUpdate"] = System.currentTimeMillis()
        params["lastMessage"] = message
//        params["userId"] = chatUserId.toString()

        params["group"] = false
        /* params["id"] = threadRef

         //Adding in sender's conversation
         mDBConversations.child(session?.user?.userId.toString()).child(threadRef)
             .updateChildren(params)

         //Adding in receiver's conversation
         params["userId"] = session?.user?.userId.toString()
         mDBConversations.child(chatUserId.toString()).child(threadRef).updateChildren(params)*/

    }

    fun destroy() {
        currentMediaPlaying.value = null
    }

    interface OnReactedListener {
        fun onDonateClicked(messageModel: Message, reactionModel: PostReactionsResponseModel)
        fun onProfileClicked(userId: String)
    }

}