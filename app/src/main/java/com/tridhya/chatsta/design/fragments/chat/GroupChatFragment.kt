package com.tridhya.chatsta.design.fragments.chat

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.inputmethod.InputContentInfoCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordPermissionHandler
import com.google.android.material.tabs.TabLayout
import com.tridhya.chatsta.Model.response.Message
import com.tridhya.chatsta.R
import com.tridhya.chatsta.R.color
import com.tridhya.chatsta.R.string
import com.tridhya.chatsta.R.style
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.FragmentGroupChatBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.dialogs.SelectMediaDialog
import com.tridhya.chatsta.design.dialogs.picker.MediaPickerBottomSheetDialog
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.chat.ChatViewModel
import com.tridhya.chatsta.enum.chat.MessageType
import com.tridhya.chatsta.enum.chat.UserStatus
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.provider.Constants.GROUP_ID
import com.tridhya.chatsta.utils.FilePathUtil
import com.tridhya.chatsta.utils.FileUtils
import com.tridhya.chatsta.utils.MyEditText
import gun0912.tedimagepicker.builder.type.MediaType
import java.io.File
import java.io.IOException
import java.lang.reflect.Member
import java.util.Calendar

class GroupChatFragment : BaseFragment(), View.OnClickListener,
    MediaPickerBottomSheetDialog.OnModeSelected {
    private lateinit var binding: FragmentGroupChatBinding
    private val viewModel by lazy { ChatViewModel(requireContext()) }
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var threadId: String? = null

    //    private var adapter: GroupChatAdapter? = null
//    private var chatEmojis: ArrayList<ChatEmojisModel>? = null
//    private var chatSoundEmojis: ArrayList<ChatEmojisModel>? = null
//    private var chatEmojisAdapter: ChatEmojisAdapter? = null
//    private val mediaList: ArrayList<MediaUris> = arrayListOf()
//    private val uploadedMedia = arrayListOf<PostMediaModel>()
    val messages = arrayListOf<Message>()

    //    private val mediaAdapter by lazy { ScheduleMessageAdapter(mediaList) }
//    private var chatSoundEmojisAdapter: ChatSoundEmojisAdapter? = null
    private var lastSeen: Long = 0
    private var isOnline: UserStatus = UserStatus.OFFLINE
    private var userSetStatus: UserStatus = UserStatus.ONLINE
//    private var mDBReference: DatabaseReference? = null
//    private var mThreadReference: DatabaseReference? = null

//    private var groupValueEventListener: ValueEventListener? = null
//    private var deletedChatListener: ValueEventListener? = null
//    private var mScheduleMessages: DatabaseReference? = null

    private var isChatDeleted = false
    private var selfDestructTime = Constants.SELF_DESTRUCT_OFF

    var memberList = arrayListOf<Member>()
    var memberMap = HashMap<String, String>()
    private var tempMessageModel: Message? = null
//    private var tempReactionModel: PostReactionsResponseModel? = null

    private var recorder: MediaRecorder? = null
    private var fileName: String? = null

    private var destructDuration: String? = Constants.SELF_DESTRUCT_OFF
    private var scheduledMessages: ArrayList<Message> = arrayListOf()
    private var scheduleMessageTimeStamp: Long? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context as ActivityBase).disableScreenShots()
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mediaPlayer.stop()
//                    adapter?.destroy()
                    findNavController().navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGroupChatBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        val arguments = arguments
        if (arguments != null) {
            threadId = arguments.getString(GROUP_ID)
        }
//        AppClass.setInChat(threadId.toString(), true)
        binding.pbEmojis.visible()
//        viewModel.getChatEmojis()
//        viewModel.getChatSoundEmojis()
//        viewModel.getChatReactions()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvScheduleMessage.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.rvScheduleMessage.adapter = mediaAdapter

//        mDBReference = AppClass.mDBReference
        initViews()
        setObservers()
        binding.ivClose.setOnClickListener(this)
        binding.ivAddMedia.setOnClickListener(this)
        binding.ivMicrophone.setOnClickListener(this)
        binding.ivEmoji.setOnClickListener(this)
        binding.ivTimer.setOnClickListener(this)
        binding.ivFire.setOnClickListener(this)
        binding.ivSend.setOnClickListener(this)
        binding.tvDelete.setOnClickListener(this)
        binding.tvDate.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)
        binding.ivSchedule.setOnClickListener(this)
        binding.btnSendEmoji.setOnClickListener(this)
        binding.tvToolbarTitle.setOnClickListener(this)
        binding.ivDeleteAudio.setOnClickListener(this)

        binding.evMessage.setOnClickListener {
            preventDoubleClick(it)
            binding.llEmojis.gone()
        }
    }

    private fun setObservers() {
        /*viewModel.responseChatEmojis.observe(viewLifecycleOwner) {
            chatEmojis = it.emoji
            chatEmojisAdapter = chatEmojis?.let { ChatEmojisAdapter(it, this) }
            binding.rvEmojis.adapter = chatEmojisAdapter
            if (binding.pbEmojis.isVisible)
                binding.pbEmojis.gone()
        }

        viewModel.responseChatSoundEmojis.observe(viewLifecycleOwner) {
            chatSoundEmojis = it.sounds
            chatSoundEmojisAdapter = chatSoundEmojis?.let { ChatSoundEmojisAdapter(it, this) }
            if (binding.pbEmojis.isVisible)
                binding.pbEmojis.gone()
        }
        viewModel.responseChatReactions.observe(viewLifecycleOwner) {
            adapter?.setChatReactions(it)
            it.forEach { reaction ->
                Glide.with(requireContext()).asGif().load(reaction.gifUrl)
            }
        }

        viewModel.responseDonateReactions.observe(viewLifecycleOwner) {
            val model = tempMessageModel
            val data = tempReactionModel
            model?.id?.let {
                mThreadReference?.child(it)?.child("reactions")
                    ?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val reactions =
                                    snapshot.value as HashMap<*, *>
                                var count = 0
                                reactions.forEach { (reactionId, reactionModel) ->
                                    if (reactionId == data?.id) {
                                        count =
                                            (reactionModel as HashMap<*, *>)["count"].toString()
                                                .toInt()
                                    }
                                }
                                val params: MutableMap<String, Any> =
                                    HashMap()
                                params["${data?.id}"] =
                                    ChatReactionsModel(
                                        data?.id,
                                        imageUrl = data?.imageUrl,
                                        data?.gifUrl,
                                        count + 1
                                    )
                                model.id?.let { str ->
                                    mThreadReference?.child(str)
                                        ?.child("reactions")
                                        ?.updateChildren(params)
                                }
                            } else {
                                val params: MutableMap<String, Any> =
                                    HashMap()
                                params["${data?.id}"] =
                                    ChatReactionsModel(
                                        data?.id,
                                        imageUrl = data?.imageUrl,
                                        data?.gifUrl,
                                        1
                                    )
                                model.id?.let {
                                    mThreadReference?.child(it)
                                        ?.child("reactions")
                                        ?.updateChildren(params)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
            }
            tempReactionModel = null
            tempMessageModel = null
            viewModel.isLoading.value = false
        }*/

    }

    private fun initViews() {
        initListeners()
        binding.rvEmojis.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())

        binding.evMessage.setKeyBoardInputCallbackListener(object :
            MyEditText.KeyBoardInputCallbackListener {
            override fun onCommitContent(
                inputContentInfo: InputContentInfoCompat?,
                flags: Int,
                opts: Bundle?,
            ) {
                try {
                    if (inputContentInfo != null) {
                        if (inputContentInfo.contentUri != null) {
                            Log.e("GIF::::", inputContentInfo.contentUri.path.toString())
//                            val uri = inputContentInfo.contentUri
//                            val size = FileUtils(requireContext()).getFileSize(uri)
//                            if (size != null && size <= 30000000) {
//                                uploadMedia(MessageType.IMAGE, uri, 1)
//                            } else {
//                                showToastShort(getString(R.string.ett_file_size))
//                            }
                        }
                        if (inputContentInfo.linkUri != null) {
                            Log.e("GIF:::", inputContentInfo.linkUri!!.path.toString())
                        }
                        Log.e("GIF:::", inputContentInfo.description.label.toString())
                    }
                } catch (ex: Exception) {
                    Log.e("GIF:::", ex.message.toString())
                }
            }
        })

        binding.tlEmojis.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
//                adapter?.destroy()
                when (tab?.position) {
                    0 -> {
                        mediaPlayer.stop()
                        binding.llChooseSound.gone()
                        binding.llReactionSound.gone()
                        binding.rvEmojis.layoutManager = GridLayoutManager(requireContext(), 3)
//                        binding.rvEmojis.adapter = chatEmojisAdapter
//                        chatSoundEmojisAdapter?.selectedEmoji = null
                    }

                    1 -> {
                        mediaPlayer.stop()
                        binding.llChooseSound.visible()
                        binding.llReactionSound.gone()
                        binding.rvEmojis.layoutManager = GridLayoutManager(requireContext(), 6)
//                        binding.rvEmojis.adapter = chatSoundEmojisAdapter
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        checkInternetAndInitialize()

    }

    private fun initListeners() {
//        mScheduleMessages =
//            mDBReference?.child(Constants.TABLE_CHAT)?.child(Constants.TABLE_SCHEDULE)
//                ?.child(session?.user?.userId.toString())
//                ?.child(threadId.toString())

        /*mScheduleMessages?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val values = snapshot.value as HashMap<*, *>?
                    if (!values.isNullOrEmpty()) {
                        val messages = values[Constants.TABLE_MESSAGES] as ArrayList<HashMap<*, *>>?
                        if (values.containsKey("timestamp") && values["timestamp"] != null)
                            scheduleMessageTimeStamp = values["timestamp"] as Long
                        if (messages?.isNotEmpty() == true) {
                            binding.ivTimerYellow.visible()
                            val msgs = arrayListOf<Message>()
                            messages.forEach { map ->
                                val message = Message()
                                message.id = map["id"] as String?
                                if (map.containsKey("message"))
                                    message.message = map["message"] as String?
                                message.mediaUrl = map["mediaUrl"] as String?
                                if (map.containsKey("thumbnailUrl"))
                                    message.thumbnailUrl = map["thumbnailUrl"] as String?
                                if (map.containsKey("fileDuration"))
                                    message.fileDuration = map["fileDuration"] as String?
                                if (map.containsKey("audioUrl"))
                                    message.audioUrl = map["audioUrl"] as String?
                                if (map.containsKey("type"))
                                    message.type = MessageType.valueOf(map["type"] as String)
                                if (map.containsKey("mediaStatus"))
                                    message.mediaStatus =
                                        MediaStatus.valueOf(map["mediaStatus"] as String)
                                if (map.containsKey("status"))
                                    message.status = MessageStatus.valueOf(map["status"] as String)

                                msgs.add(message)
                            }
                            scheduledMessages = msgs
                        } else {
                            binding.ivTimerYellow.gone()
                            scheduledMessages.clear()
                        }
                    } else {
                        binding.ivTimerYellow.gone()
                        scheduledMessages.clear()
                    }
                } else {
                    binding.ivTimerYellow.gone()
                    scheduledMessages.clear()
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })

        groupValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val values = dataSnapshot.value as HashMap<*, *>?
                if (values != null) {
                    binding.tvToolbarTitle.text = values["name"].toString()
                    binding.tvGroupName.text = values["name"].toString()

                    memberList = arrayListOf()

                    dataSnapshot.child("members").children.forEach {
                        val member = it.getValue(Member::class.java)
                        member?.let { it1 -> memberList.add(it1) }
                    }

                    if (memberList.isNotEmpty()) {
                        binding.tvToolbarUserStatus.text = "${memberList.size} Group Members"
                        binding.tvGroupMembers.text = "${memberList.size} Group Members"
                        observeUser(memberList)
                    }

                    val member = dataSnapshot.child("members")
                        .child(session?.user?.userId.toString())
                        .getValue(Member::class.java)

                    if (member == null) {
                        findNavController().popBackStack()//will remove user from screen if removed from group while ongoing chat
                    } else {
                        val cal = Calendar.getInstance()
                        if (member.lastDeleted != null) {
                            cal.timeInMillis = member.lastDeleted!!
                        } else {
                            cal.timeInMillis = member.doj
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showToastLong(databaseError.message)
            }
        }

        deletedChatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value as Boolean? == true) {
                    deleteChatAnimation()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }*/
    }

    private fun deleteChatAnimation() {
//        (requireActivity() as ActivityBase).showGIF(raw.fire_flood_animation)
        Handler(Looper.getMainLooper()).postDelayed({
            deleteChat()
            findNavController().navigateUp()
        }, 3800)
    }

    private fun observeUser(model: ArrayList<Member>) {
        binding.llProfile2.isVisible = model.size > 1
        /*for (i in model.indices) {
            mDBReference?.child(Constants.TABLE_USERS)?.child(model[i].id.toString())
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val values = dataSnapshot.value as HashMap<*, *>?
                        Log.e("DataSnapshot", dataSnapshot.value.toString())

                        if (values != null) {

                            if (values.containsKey("name") && values["name"].toString()
                                    .isNotEmpty() && values["name"].toString().isNotBlank()
                            ) {
                                memberMap.put(model[i].id.toString(), values["name"].toString())
                            }
                            if (i == 0) {

                                if (values.containsKey("image") && values["image"].toString()
                                        .isNotEmpty() && values["image"].toString().isNotBlank()
                                )
                                    GlideUtils(binding.ivProfileImage.context).circleImage()
                                        .loadImage(
                                            values["image"].toString(),
                                            binding.ivProfileImage
                                        )
                                else
                                    GlideUtils(binding.ivProfileImage.context).circleImage()
                                        .loadImage(R.drawable.ic_app, binding.ivProfileImage)

                                if (values.containsKey("lastSeen") && values["lastSeen"] != null)
                                    lastSeen = values["lastSeen"] as Long
                                if (values.containsKey("isOnline") && values["isOnline"] != null)
                                    isOnline = UserStatus.valueOf(values["isOnline"] as String)
                                userSetStatus =
                                    if (values.containsKey("userSetStatus") && values["userSetStatus"] != null && values["userSetStatus"].toString()
                                            .isNotBlank()
                                    ) UserStatus.valueOf(values["userSetStatus"] as String) else UserStatus.ONLINE
                                if (values["inChatWith"] == null || values["inChatWith"].toString()
                                        .isBlank() || values["inChatWith"].toString() == threadId
                                ) {
                                    if (userSetStatus == UserStatus.ONLINE) {
                                        when (isOnline) {
                                            UserStatus.ONLINE -> {
                                                binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_green)
                                            }

                                            UserStatus.BUSY -> {
                                                binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_blue)
                                            }

                                            else -> {
                                                binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_red)
                                            }
                                        }
                                    } else {
                                        when (userSetStatus) {
                                            UserStatus.ONLINE -> {
                                                binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_green)
                                            }

                                            UserStatus.BUSY -> {
                                                binding.ivProfileIndicator.setImageResource(R.drawable.ic_busy_status)
                                            }

                                            else -> {
                                                binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_red)
                                            }
                                        }
                                    }
                                } else {
                                    if (userSetStatus == UserStatus.ONLINE) {
                                        binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_blue)
                                    } else {
                                        when (userSetStatus) {
                                            UserStatus.ONLINE -> {
                                                binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_green)
                                            }

                                            UserStatus.BUSY -> {
                                                binding.ivProfileIndicator.setImageResource(R.drawable.ic_busy_status)
                                            }

                                            else -> {
                                                binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_red)
                                            }
                                        }
                                    }
                                }
                            } else if (i == 1) {
                                if (values.containsKey("image") && values["image"].toString()
                                        .isNotEmpty() && values["image"].toString().isNotBlank()
                                )
                                    GlideUtils(binding.ivProfileImage.context).circleImage()
                                        .loadImage(
                                            values["image"].toString(),
                                            binding.ivProfileImage2
                                        )
                                else
                                    GlideUtils(binding.ivProfileImage.context).circleImage()
                                        .loadImage(R.drawable.ic_app, binding.ivProfileImage2)

                                if (values.containsKey("lastSeen") && values["lastSeen"] != null)
                                    lastSeen = values["lastSeen"] as Long
                                if (values.containsKey("isOnline") && values["isOnline"] != null)
                                    isOnline = UserStatus.valueOf(values["isOnline"] as String)
                                userSetStatus =
                                    if (values.containsKey("userSetStatus") && values["userSetStatus"] != null && values["userSetStatus"].toString()
                                            .isNotBlank()
                                    ) UserStatus.valueOf(values["userSetStatus"] as String) else UserStatus.ONLINE
                                if (values["inChatWith"] == null || values["inChatWith"].toString()
                                        .isBlank() || values["inChatWith"].toString() == threadId
                                ) {
                                    if (userSetStatus == UserStatus.ONLINE) {
                                        when (isOnline) {
                                            UserStatus.ONLINE -> {
                                                binding.ivProfileIndicator2.setImageResource(R.drawable.v_ic_circle_green)
                                            }

                                            UserStatus.BUSY -> {
                                                binding.ivProfileIndicator2.setImageResource(R.drawable.v_ic_circle_blue)
                                            }

                                            else -> {
                                                binding.ivProfileIndicator2.setImageResource(R.drawable.v_ic_circle_red)
                                            }
                                        }
                                    } else {
                                        when (userSetStatus) {
                                            UserStatus.ONLINE -> {
                                                binding.ivProfileIndicator2.setImageResource(R.drawable.v_ic_circle_green)
                                            }

                                            UserStatus.BUSY -> {
                                                binding.ivProfileIndicator2.setImageResource(R.drawable.ic_busy_status)
                                            }

                                            else -> {
                                                binding.ivProfileIndicator2.setImageResource(R.drawable.v_ic_circle_red)
                                            }
                                        }
                                    }
                                } else {
                                    if (userSetStatus == UserStatus.ONLINE) {
                                        binding.ivProfileIndicator2.setImageResource(R.drawable.v_ic_circle_blue)
                                    } else {
                                        when (userSetStatus) {
                                            UserStatus.ONLINE -> {
                                                binding.ivProfileIndicator2.setImageResource(R.drawable.v_ic_circle_green)
                                            }

                                            UserStatus.BUSY -> {
                                                binding.ivProfileIndicator2.setImageResource(R.drawable.ic_busy_status)
                                            }

                                            else -> {
                                                binding.ivProfileIndicator2.setImageResource(R.drawable.v_ic_circle_red)
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }

        mDBReference?.child(Constants.TABLE_CHAT)?.child(Constants.TABLE_GROUPS)
            ?.child(threadId.toString())?.child("isTyping")
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value != null) {
                        var count = 0
                        val members = dataSnapshot.value as HashMap<*, *>
                        var str = ""
                        members.keys.forEach {
                            if (it != session?.user?.userId) {
                                val isTyping = members[it] as Boolean
                                if (isTyping) {
                                    if (memberMap.containsKey(it)) {
                                        count++
//                                        if (!str.isNullOrBlank()) {
//                                            str += ", "
//                                        }
                                        if (count == 1)
                                            str += "${memberMap[it]}"
                                    }
                                }
                            }
                        }

                        binding.llTypingStatus.isVisible = str.isNotBlank()
                        if (count == 1)
                            binding.tvTypingStatus.text = "$str is typing"
                        else if (count > 1)
                            binding.tvTypingStatus.text = "$count people are typing"

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
*/
    }

    private fun checkInternetAndInitialize() {
        val chatParams: MutableMap<String, Any> = HashMap()
        chatParams["inChatWith"] = threadId.toString()
//        updateUserStatus(false)

        /* mDBReference
             ?.child(Constants.TABLE_USERS)
             ?.child(session?.user?.userId.toString())
             ?.updateChildren(chatParams)

         mThreadReference = mDBReference
             ?.child(Constants.TABLE_CHAT)
             ?.child(Constants.TABLE_THREADS)
             ?.child(threadId.toString())

         groupValueEventListener?.let {
             mDBReference
                 ?.child(Constants.TABLE_CHAT)
                 ?.child(Constants.TABLE_GROUPS)
                 ?.child(threadId.toString())
                 ?.addValueEventListener(it)
         }*/

        binding.evMessage.doOnTextChanged { text, start, before, count ->
            if (start != 0 || before != 0) {
                updateUserStatus(true)
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(runnable, 1500)
            }
        }
        getSelfDestruct()
        resetUnreadCount()
        setAdapter()
        checkDeletedChat()
    }

    private fun scrollDown() {
        /*if (adapter != null) {
            val viewHolder =
                binding.rvMessages.findViewHolderForAdapterPosition(adapter!!.itemCount - 1)
            viewHolder?.itemView?.y?.toInt()?.let {
                binding.nestedScrollView.smoothScrollTo(
                    0,
                    it
                )
            }
        }*/
    }

    val handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = Runnable {
        updateUserStatus(false)
    }

    private fun updateUserStatus(isTyping: Boolean) {
        val myUserId = session?.user?.userId.toString()

        val params: MutableMap<String, Any> = HashMap()
        params[myUserId] = isTyping

        /*mDBReference
            ?.child(Constants.TABLE_CHAT)
            ?.child(Constants.TABLE_GROUPS)
            ?.child(threadId.toString())//thread
            ?.child("isTyping")
            ?.updateChildren(params)*/
    }

    private fun setAdapter() {
        val mLayoutManager = LinearLayoutManager(requireContext())
        binding.rvMessages.layoutManager = mLayoutManager
        binding.rvMessages.itemAnimator = null

        /*val query = mDBReference
            ?.child(Constants.TABLE_CHAT)
            ?.child(Constants.TABLE_THREADS)
            ?.child(threadId.toString())
            ?.orderByChild("timestamp")

        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(query!!, Message::class.java)
            .setLifecycleOwner(requireActivity())
            .build()
        if (adapter == null || adapter?.itemCount == 0) {
            adapter = GroupChatAdapter(options, threadId.toString(), this)
            if (!viewModel.responseChatReactions.value.isNullOrEmpty()) {
                adapter?.setChatReactions(viewModel.responseChatReactions.value!!)
            }
        }*/
//        binding.rvMessages.adapter = adapter
        binding.rvMessages.post {
            Handler(Looper.getMainLooper()).postDelayed(
                { scrollDown() },
                100
            )
        }
    }

    override fun onResume() {
        super.onResume()
        (context as ActivityBase).disableScreenShots()
//        AppClass.setInChat(threadId.toString(), true)
//        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
//            "chatDeleted"
//        )?.observe(viewLifecycleOwner) {
//            if (it) {
//                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(
//                    "chatDeleted"
//                )
//                binding.ivMsgBurn.visible()
//                Glide.with(requireContext()).asGif().load(R.raw.fire_flood_animation)
//                    .into(binding.ivMsgBurn)
//                Handler(Looper.getMainLooper()).postDelayed({
//                    binding.ivMsgBurn.gone()
//                    deleteChat()
//                    findNavController().navigateUp()
//                }, 3500)
//            }
//        }

        /* val chatWith = ChatWith()
         chatWith.isGroup = true
         chatWith.userId = threadId
 */
        /* if (mDBReference != null) {
             mDBReference!!
                 .child(Constants.TABLE_USERS).child(session?.user?.userId.toString())
                 .child("chatWith")
                 .setValue(chatWith)
         }
 */
        binding.rvMessages.post {
            Handler(Looper.getMainLooper()).postDelayed(
                { scrollDown() },
                100
            )
        }

        val params: MutableMap<String, Any> = HashMap()
        params["inChatWith"] = threadId.toString()
        /*mDBReference
            ?.child(Constants.TABLE_USERS)
            ?.child(session?.user?.userId.toString())
            ?.updateChildren(params)

        val lastOnlineInChatParams: MutableMap<String, Any> = HashMap()
        lastOnlineInChatParams["lastOnlineInChat"] = System.currentTimeMillis()
        mDBReference
            ?.child(Constants.TABLE_CHAT)
            ?.child(Constants.TABLE_CONVERSATIONS)
            ?.child(session?.user?.userId.toString())
            ?.child(threadId.toString())
            ?.updateChildren(lastOnlineInChatParams)*/
    }

    private fun sendMessage() {
        if (binding.evMessage.text.toString().trim().isNotEmpty()) {
//            val messageId = mThreadReference?.push()?.key
//            val aes = SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
            /*val encryptedMessage = aes.encryption(binding.evMessage.text.toString())
            val message = if (!destructDuration.equals(Constants.SELF_DESTRUCT_OFF)) {
                Message(
                    messageId.toString(), session?.user?.userId.toString(), encryptedMessage,
                    selfDestruct = destructDuration
                )
            } else {
                Message(
                    messageId.toString(), session?.user?.userId.toString(), encryptedMessage,
                    selfDestruct = selfDestructTime
                )
            }*/
            /*mThreadReference?.child(messageId.toString())?.setValue(message)
            mThreadReference?.child(messageId.toString())?.child("readBy")
                ?.updateChildren(mapOf(session?.user?.userId to true))
            binding.evMessage.setText("")
*/
            /*mDBReference
                ?.child(Constants.TABLE_CHAT)
                ?.child(Constants.TABLE_GROUPS)
                ?.child(threadId.toString())
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val values = dataSnapshot.value as HashMap<*, *>?

                        if (values != null) {
                            val mDBConversations = mDBReference?.child(Constants.TABLE_CHAT)
                                ?.child(Constants.TABLE_CONVERSATIONS)
                            val members = dataSnapshot.child("members")
                            //Injecting last message to conversation
                            for (i in members.children) {
                                val userConv = mDBConversations
                                    ?.child(i.key.toString())
                                    ?.child(threadId.toString())

                                userConv?.child("unreadCount")
                                    ?.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {
                                        }

                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            var count =
                                                if (snapshot.exists()) snapshot.value as Long else 0
                                            if (!session?.user?.userId.equals(i.key.toString())) {
                                                count += 1
                                            }
                                            //Adding in receiver's conversation
                                            val conversation = Conversation(
                                                threadId,
                                                true,
                                                values["name"].toString(),
                                                values["image"].toString(),
                                                count,
                                                message,
                                                selfDestruct = selfDestructTime,
                                                lastUpdate = System.currentTimeMillis()
                                            )
                                            conversation.setRequested(if (values.containsKey("isChatMuted") && values["isChatMuted"] != null) values["isChatMuted"] as Boolean else false)
                                            userConv.setValue(conversation)
                                            Handler(Looper.getMainLooper()).postDelayed(
                                                {
                                                    scrollDown()
                                                    destructDuration = Constants.SELF_DESTRUCT_OFF
                                                    binding.ivBurnYellow.gone()
                                                },
                                                100
                                            )
                                        }
                                    })
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })*/
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.clearFlags(
            WindowManager.LayoutParams.FLAG_SECURE
        )
        /* adapter?.destroy()
         groupValueEventListener?.let {
             mDBReference
                 ?.child(Constants.TABLE_CHAT)
                 ?.child(Constants.TABLE_GROUPS)
                 ?.child(threadId.toString())
                 ?.removeEventListener(it)
         }*/

        /* if (mDBReference != null) {
             mDBReference!!
                 .child(Constants.TABLE_USERS).child(session?.user?.userId.toString())
                 .child("chatWith").removeValue()
             mDBReference!!
                 .child(Constants.TABLE_USERS).child(session?.user?.userId.toString())
                 .child("inChatWith").removeValue()

             if (!isChatDeleted) {
                 val lastOnlineInChatParams: MutableMap<String, Any> = HashMap()
                 lastOnlineInChatParams["lastOnlineInChat"] = System.currentTimeMillis()
                 mDBReference
                     ?.child(Constants.TABLE_CHAT)
                     ?.child(Constants.TABLE_CONVERSATIONS)
                     ?.child(session?.user?.userId.toString())
                     ?.child(threadId.toString())
                     ?.updateChildren(lastOnlineInChatParams)
             }
             updateUserStatus(false)
         }

         if (deletedChatListener != null) {
             resetUnreadCount()
             mDBReference
                 ?.child(Constants.TABLE_CHAT)
                 ?.child(Constants.TABLE_GROUPS)
                 ?.child(threadId.toString())
                 ?.child("isDeleting")
                 ?.removeEventListener(deletedChatListener!!)
         }

         handler.removeCallbacks(runnable)
         AppClass.setInChat("0", false)*/
    }

    private fun checkDeletedChat() {
//        if (deletedChatListener != null) {
//            mDBReference
//                ?.child(Constants.TABLE_CHAT)
//                ?.child(Constants.TABLE_GROUPS)
//                ?.child(threadId.toString())
//                ?.child("isDeleting")
//                ?.addValueEventListener(deletedChatListener!!)
//        }
    }

    private fun deleteChat() {
        isChatDeleted = true
        memberList.forEach {
            /*it.id?.let { it1 ->
                threadId?.let { it2 ->
                    mDBReference
                        ?.child(Constants.TABLE_CHAT)
                        ?.child(Constants.TABLE_CONVERSATIONS)
                        ?.child(it1)
                        ?.child(it2)
                        ?.removeValue()
                }
            }*/
        }
//        threadId?.let {
//            mDBReference
//                ?.child(Constants.TABLE_CHAT)
//                ?.child(Constants.TABLE_GROUPS)
//                ?.child(it)
//                ?.removeValue()
//        }
//        threadId?.let {
//            mDBReference
//                ?.child(Constants.TABLE_CHAT)
//                ?.child(Constants.TABLE_THREADS)
//                ?.child(it)
//                ?.removeValue()
//        }
    }

//    private fun checkAdmin() {
//        mDBReference
//            ?.child(Constants.TABLE_CHAT)
//            ?.child(Constants.TABLE_GROUPS)
//            ?.child(threadId.toString())
//            ?.child("members")
//            ?.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(ds: DataSnapshot) {
//                    val list = java.util.ArrayList<Member>()
//                    ds.children.forEach { it ->
//                        it.getValue(Member::class.java)?.let {
//                            list.add(it)
//                        }
//                    }
//
//                    var adminCount = 0
//                    var meAdmin = false
//                    for (member in list) {
//                        if (member.id.equals(session?.user?.userId)) {
//                            meAdmin = true
//                            adminCount += 1
//                        } else if (member.isAdmin) {
//                            adminCount += 1
//                        }
//                    }
//
//                    if (meAdmin && adminCount <= 1) {
//                        AlertDialog.Builder(requireContext())
//                            .setMessage(getString(com.chatsta.R.string.please_make_admin))
//                            .setPositiveButton(
//                                getString(com.chatsta.R.string.ok)
//                            ) { dialog, which ->
//                                dialog?.dismiss()
//                                val intent =
//                                    Intent(requireContext(), GroupInfoActivity::class.java)
//                                intent.putExtra(Constants.ID, threadId)
//                                startActivity(intent)
//                            }.setNegativeButton(
//                                getString(R.string.cancel)
//                            ) { dialog, which ->
//                                dialog?.dismiss()
//                            }.show()
//                    } else {
//                        AlertDialog.Builder(this@GroupChatActivity)
//                            .setMessage(getString(R.string.alert_message_on_exit_chat_group))
//                            .setPositiveButton(
//                                getString(R.string.yes)
//                            ) { dialog, which ->
//                                dialog?.dismiss()
//                                exitFromGroup()
//                            }
//                            .setNegativeButton(
//                                getString(R.string.cancel)
//                            ) { dialog, which ->
//                                dialog?.dismiss()
//                            }.show()
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    MessageDialog.getInstance(this@GroupChatActivity, error.message).show()
//                }
//            })
//    }

    private fun resetUnreadCount() {
        //Reset read count
        /* mDBReference
             ?.child(Constants.TABLE_CHAT)
             ?.child(Constants.TABLE_CONVERSATIONS)
             ?.child(session?.user?.userId.toString())
             ?.child(threadId.toString())
             ?.addListenerForSingleValueEvent(object : ValueEventListener {
                 override fun onCancelled(error: DatabaseError) {

                 }

                 override fun onDataChange(dataSnapshot: DataSnapshot) {
                     if (dataSnapshot.value != null) {
                         val params: MutableMap<String, Any> = HashMap()
                         params["unreadCount"] = 0
                         mDBReference
                             ?.child(Constants.TABLE_CHAT)
                             ?.child(Constants.TABLE_CONVERSATIONS)
                             ?.child(session?.user?.userId.toString())
                             ?.child(threadId.toString())
                             ?.updateChildren(params)
                     }
                 }
             })*/
    }

    private fun getSelfDestruct() {
        /* mDBReference
             ?.child(Constants.TABLE_CHAT)
             ?.child(Constants.TABLE_CONVERSATIONS)
             ?.child(session?.user?.userId.toString())//Receiver
             ?.child(threadId.toString())
             ?.child("selfDestruct")
             ?.addValueEventListener(object : ValueEventListener {
                 override fun onDataChange(dataSnapshot: DataSnapshot) {
                     selfDestructTime = if (dataSnapshot.value != null) {
                         dataSnapshot.value as String
                     } else {
                         Constants.SELF_DESTRUCT_OFF
                     }
                 }

                 override fun onCancelled(error: DatabaseError) {
                     showToastLong(error.message)
                 }
             })*/
    }

    private fun sendEmojiMessage(emojiUrl: String, audioUrl: String = "", type: MessageType) {
//        val messageId = mThreadReference?.push()?.key
//        val aes = SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
        /* val encryptedMessage = aes.encryption(emojiUrl)
         val encryptedUrl = if (audioUrl.isBlank()) audioUrl else aes.encryption(audioUrl)
         val message = if (!destructDuration.equals(Constants.SELF_DESTRUCT_OFF)) {
             Message(
                 encryptedMessage.toString(),
                 encryptedUrl.toString(),
                 messageId.toString(),
                 session?.user?.userId.toString(),
                 type,
                 selfDestruct = destructDuration
             )
         } else {
             Message(
                 encryptedMessage.toString(),
                 encryptedUrl.toString(),
                 messageId.toString(),
                 session?.user?.userId.toString(),
                 type,
                 selfDestruct = selfDestructTime
             )
         }*/

        /*mThreadReference?.child(messageId.toString())?.setValue(message)
        mThreadReference?.child(messageId.toString())?.child("readBy")
            ?.updateChildren(mapOf(session?.user?.userId to true))
*/
        //Clearing text filed
        binding.evMessage.setText("")
        /* mDBReference
             ?.child(Constants.TABLE_CHAT)
             ?.child(Constants.TABLE_GROUPS)
             ?.child(threadId.toString())
             ?.addListenerForSingleValueEvent(object : ValueEventListener {
                 override fun onDataChange(dataSnapshot: DataSnapshot) {
                     val values = dataSnapshot.value as HashMap<*, *>?

                     if (values != null) {
                         val mDBConversations = mDBReference?.child(Constants.TABLE_CHAT)
                             ?.child(Constants.TABLE_CONVERSATIONS)
                         val members = dataSnapshot.child("members")
                         //Injecting last message to conversation
                         for (i in members.children) {
                             val userConv = mDBConversations
                                 ?.child(i.key.toString())
                                 ?.child(threadId.toString())

                             userConv?.child("unreadCount")
                                 ?.addListenerForSingleValueEvent(object : ValueEventListener {
                                     override fun onCancelled(p0: DatabaseError) {
                                     }

                                     override fun onDataChange(p0: DataSnapshot) {
                                         var count = p0.value as Long
                                         if (!session?.user?.userId.equals(i.key.toString())) {
                                             count += 1
                                         }
                                         //Adding in receiver's conversation
                                         val conversation = Conversation(
                                             threadId,
                                             true,
                                             values["name"].toString(),
                                             values["image"].toString(),
                                             count,
                                             message,
                                             selfDestruct = selfDestructTime,
                                             lastUpdate = System.currentTimeMillis()
                                         )
                                         conversation.setRequested(if (values.containsKey("isChatMuted") && values["isChatMuted"] != null) values["isChatMuted"] as Boolean else false)
                                         userConv.setValue(conversation)

                                         Handler(Looper.getMainLooper()).postDelayed(
                                             {
                                                 scrollDown()
                                                 destructDuration = Constants.SELF_DESTRUCT_OFF
                                                 binding.ivBurnYellow.gone()
                                             },
                                             100
                                         )

                                     }
                                 })
                         }
                     }
                 }

                 override fun onCancelled(databaseError: DatabaseError) {}
             })*/
    }

    /* override fun onChatEmojiSelected(emojiData: ChatEmojisModel) {
         mediaPlayer.stop()
         adapter?.destroy()
         emojiData.imageUrl?.let { it1 ->
             if (binding.ivSchedule.isVisible) {
                 if (mediaList.size < 5) {
                     binding.rvScheduleMessage.isVisible = true
                     mediaList.add(
                         MediaUris(
                             com.chatsta.model.feed.MediaType.EMOJI,
                             url = emojiData.imageUrl
                         )
                     )
                     mediaAdapter.notifyDataSetChanged()
                 } else {
                     showToastLong("Max Limit : 5")
                 }
             } else {
                 (requireContext() as MainActivity).showChatEmoji(it1)
                 sendEmojiMessage(emojiUrl = emojiData.imageUrl.toString(), type = MessageType.EMOJI)
             }
         }
     }


     override fun onSoundEmojiSelected(emojiData: ChatEmojisModel) {
         mediaPlayer.stop()
         adapter?.destroy()
         if (binding.tlEmojis.selectedTabPosition == 1) {
             binding.llChooseSound.gone()
             binding.llReactionSound.visible()
             binding.pbReactionSound.visible()
             GlideUtils(requireContext()).loadImage(emojiData.imageUrl, binding.ivSelectedEmoji)
             mediaPlayer.apply {
                 reset()
                 setAudioStreamType(AudioManager.STREAM_MUSIC)
                 setDataSource(emojiData.audioUrl)
                 prepareAsync()
             }
             mediaPlayer.setOnPreparedListener {
                 binding.pbReactionSound.gone()
                 mediaPlayer.start()
             }
         } else {
             mediaPlayer.stop()
             binding.llChooseSound.gone()
             binding.llReactionSound.gone()
         }
     }
 */
    override fun onClick(view: View?) {
        mediaPlayer.stop()
//        adapter?.destroy()
        when (view?.id) {
            R.id.ivClose -> {
                preventDoubleClick(view)
                findNavController().navigateUp()
            }

            R.id.ivDeleteAudio -> {
                if (binding.cvRecordView.isVisible) {
                    binding.cvRecordView.gone()
                    binding.llMainActions.visible()
                }
            }

            R.id.ivAddMedia -> {
                preventDoubleClick(view)
                hideKeyboard()
                binding.llEmojis.gone()
                openMediaPicker()
            }

            R.id.ivMicrophone -> {
                preventDoubleClick(view)
                hideKeyboard()
                binding.llEmojis.gone()
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.RECORD_AUDIO), Constants.RECORD_AUDIO
                    )
                } else {
                    binding.llMainActions.gone()
                    binding.cvRecordView.visible()
//                    initRecord()
                    recorderView()
                }
            }

            R.id.ivEmoji -> {
                preventDoubleClick(view)
                hideKeyboard()
                binding.llEmojis.visible()
            }

            R.id.ivFire -> {
                preventDoubleClick(view)
                hideKeyboard()
                binding.llEmojis.gone()
                /*SelfDestructMessageDialog.getInstance(
                    requireContext(),
                    destructDuration.toString()
                ).setListener(object :
                    SelfDestructMessageDialog.ButtonListener {
                    override fun onDurationSelected(
                        dialog: SelfDestructMessageDialog,
                        duration: String,
                    ) {
                        dialog.dismiss()
                        destructDuration = duration
                        if (duration != Constants.SELF_DESTRUCT_OFF) {
                            binding.ivBurnYellow.visible()
                        } else {
                            binding.ivBurnYellow.gone()
                        }
                    }
                }).show()*/
            }

            R.id.tvDelete -> {
                preventDoubleClick(view)
                hideKeyboard()
                MessageDialog.getInstance(
                    requireContext(),
                    getString(string.delete_schedule_msg_txt),
                    style.ErrorThemeDialog
                )
                    .setPositiveButtonText(string.delete)
                    .setNegativeButton(getString(string.cancel))
                    .setListener(object : MessageDialog.ButtonListener {
                        override fun onPositiveBtnClicked(dialog: MessageDialog) {
                            dialog.dismiss()
                            binding.tvDate.tag = null
                            binding.llEmojis.gone()
                            binding.llScheduleMessage.gone()
                            binding.rvScheduleMessage.gone()
                            binding.evMessage.setText("")
                            binding.tvDate.text = ""
                            binding.ivSend.visible()
                            binding.ivSchedule.gone()
                            binding.ivTimer.visible()
                            binding.ivFire.visible()
//                            mediaAdapter.clearList()
//                            mediaList.clear()
//                            mediaAdapter.notifyDataSetChanged()
                            messages.clear()
//                            mScheduleMessages?.removeValue()
                            scheduledMessages.clear()
                            binding.tvDate.tag = null
                            binding.tvDate.text = ""
                            binding.ivTimerYellow.gone()
                        }
                    })
                    .show()
            }

            R.id.tvCancel -> {
                preventDoubleClick(view)
                hideKeyboard()
                MessageDialog.getInstance(
                    requireContext(),
                    getString(string.cancel_schedule_msg_txt),
                    style.DefaultThemeDialog
                )
                    .setPositiveButtonText(string.continue_txt)
                    .setNegativeButton(getString(string.cancel))
                    .setListener(object : MessageDialog.ButtonListener {
                        override fun onPositiveBtnClicked(dialog: MessageDialog) {
                            dialog.dismiss()
                            binding.tvDate.tag = null
                            binding.llEmojis.gone()
                            binding.llScheduleMessage.gone()
                            binding.evMessage.setText("")
                            binding.tvDate.text = ""
                            binding.ivSend.visible()
                            binding.ivSchedule.gone()
                            binding.ivTimer.visible()
                            binding.ivFire.visible()
//                            mediaList.clear()
//                            mediaAdapter.notifyDataSetChanged()
                            messages.clear()
                        }
                    })
                    .show()
            }

            R.id.ivTimer -> {
                preventDoubleClick(view)
                hideKeyboard()
                if (scheduledMessages.isEmpty()) {
                    binding.tvDate.tag = null
                    binding.llScheduleMessage.visible()
                    binding.llEmojis.gone()
                    binding.ivSend.gone()
                    binding.ivSchedule.visible()
                    binding.ivTimer.gone()
                    binding.rvScheduleMessage.gone()
                    binding.ivFire.gone()
                    binding.evMessage.setText("")
                } else {
                    binding.tvDate.tag = scheduleMessageTimeStamp
                    /*binding.tvDate.text = scheduleMessageTimeStamp?.let {
                        TimeStamp.millisToFormat(
                            it,
                            TimeStamp.FULL_DATE_FORMAT_SCHEDULE
                        )
                    }*/
                    binding.llScheduleMessage.visible()
                    binding.llEmojis.gone()
                    binding.ivSend.gone()
                    binding.ivSchedule.visible()
                    binding.rvScheduleMessage.visible()
                    binding.ivTimer.gone()
                    binding.ivFire.gone()
                    binding.evMessage.setText("")
                    scheduledMessages.forEach {
                        when (it.type) {
                            MessageType.IMAGE -> {
                                /*  val data = MediaUris()
                                  data.url = try {
                                      val aes =
                                          SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                                      aes.decryption(it.mediaUrl)
                                  } catch (e: Exception) {
                                      it.mediaUrl
                                  }
                                  data.mediaType = com.chatsta.model.feed.MediaType.IMAGE
                                  mediaList.add(data)*/
                            }

                            MessageType.EMOJI -> {
                                /*val data = MediaUris()
                                data.url = try {
                                    val aes =
                                        SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                                    aes.decryption(it.mediaUrl)
                                } catch (e: Exception) {
                                    it.mediaUrl
                                }
                                data.mediaType = com.chatsta.model.feed.MediaType.EMOJI
                                mediaList.add(data)*/
                            }

                            MessageType.VIDEO -> {
                                /*val data = MediaUris()
                                data.url = try {
                                    val aes =
                                        SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                                    aes.decryption(it.mediaUrl)
                                } catch (e: Exception) {
                                    it.mediaUrl
                                }
                                data.duration = it.fileDuration
                                data.thumbnailUrl = try {
                                    val aes =
                                        SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                                    aes.decryption(it.thumbnailUrl)
                                } catch (e: Exception) {
                                    it.thumbnailUrl
                                }
                                data.mediaType = com.chatsta.model.feed.MediaType.VIDEO
                                mediaList.add(data)*/
                            }

                            MessageType.AUDIO -> {
                                /*val data = MediaUris()
                                data.url = try {
                                    val aes =
                                        SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                                    aes.decryption(it.mediaUrl)
                                } catch (e: Exception) {
                                    it.mediaUrl
                                }
                                data.duration = it.fileDuration
                                data.mediaType = com.chatsta.model.feed.MediaType.AUDIO
                                mediaList.add(data)*/
                            }

                            MessageType.SOUND_EMOJI -> {
                                /*val data = MediaUris()
                                data.url = try {
                                    val aes =
                                        SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                                    aes.decryption(it.mediaUrl)
                                } catch (e: Exception) {
                                    it.mediaUrl
                                }
                                data.thumbnailUrl = try {
                                    val aes =
                                        SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                                    aes.decryption(it.audioUrl)
                                } catch (e: Exception) {
                                    it.audioUrl
                                }
                                data.mediaType = com.chatsta.model.feed.MediaType.SOUND_EMOJI
                                mediaList.add(data)*/
                            }

                            MessageType.FILE -> {
                                /* val data = MediaUris()
                                 data.url = try {
                                     val aes =
                                         SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                                     aes.decryption(it.mediaUrl)
                                 } catch (e: Exception) {
                                     it.mediaUrl
                                 }*/
//                                data.thumbnailUrl = try {
//                                    val aes =
//                                        SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
//                                    aes.decryption(it.audioUrl)
//                                } catch (e: Exception) {
//                                    it.audioUrl
//                                }
//                                data.mediaType = com.chatsta.model.feed.MediaType.FILE
//                                mediaList.add(data)
                            }

                            MessageType.TEXT -> {
                                /*val message = try {
                                   *//* val aes =
//                                        SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                                    aes.decryption(it.message)*//*
                                } catch (e: Exception) {
                                    it.message
                                }
                                binding.evMessage.setText(message)*/
                            }

                            else -> {
//                                val message = try {
//                                    val aes =
//                                        SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
//                                    aes.decryption(it.message)
//                                } catch (e: Exception) {
//                                    it.message
//                                }
//                                binding.evMessage.setText(message)
                            }
                        }
                    }
//                    mediaAdapter.notifyDataSetChanged()
                }
            }

            R.id.ivSchedule -> {
                preventDoubleClick(view)
                hideKeyboard()
                if (binding.tvDate.tag != null) {
                    /* if (!mediaList.isEmpty() || binding.evMessage.text.toString().trim()
                             .isNotEmpty() || binding.evMessage.text.toString().trim().isNotBlank()
                     ) {
                         viewModel.isLoading.value = true
                         binding.llScheduleMessage.gone()
                         binding.rvScheduleMessage.gone()
                         binding.llEmojis.gone()
                         binding.ivSend.visible()
                         binding.ivSchedule.gone()
                         binding.ivTimer.visible()
                         binding.ivFire.visible()
                         if (mediaList.isEmpty()) {
                             scheduleMessage()
                         } else {
                             messages.clear()
                             for (i in mediaList.indices) {
                                 if (mediaList[i].uri != null) {
                                     mediaList[i].uri?.let {
                                         scheduleUploadMedia(
                                             when (mediaList[i].mediaType) {
                                                 com.chatsta.model.feed.MediaType.IMAGE -> MessageType.IMAGE
                                                 com.chatsta.model.feed.MediaType.VIDEO -> MessageType.VIDEO
                                                 com.chatsta.model.feed.MediaType.AUDIO -> MessageType.AUDIO
                                                 com.chatsta.model.feed.MediaType.FILE -> MessageType.FILE
                                                 else -> MessageType.TEXT
                                             }, it, i.toLong(), mediaList[i].duration.toString()
                                         )
                                     }
                                 } else if (!mediaList[i].url.isNullOrBlank()) {
                                     if (mediaList[i].mediaType != null && !(mediaList[i].mediaType == com.chatsta.model.feed.MediaType.SOUND_EMOJI || mediaList[i].mediaType == com.chatsta.model.feed.MediaType.EMOJI)) {
                                         scheduledMessages(mediaList[i])
                                     } else {
                                         if (!mediaList[i].thumbnailUrl.isNullOrBlank()) {
                                             scheduleEmojiMessage(
                                                 MessageType.SOUND_EMOJI,
                                                 mediaList[i].url.toString(),
                                                 mediaList[i].thumbnailUrl
                                             )
                                         } else {
                                             scheduleEmojiMessage(
                                                 MessageType.EMOJI,
                                                 mediaList[i].url.toString()
                                             )
                                         }
                                     }
                                 }
                             }
                         }
                     } else {
                         showToastShort(getString(string.err_create_post))
                     }*/
                } else {
                    showToastShort(getString(string.err_select_schedule_time))
                }
            }

            R.id.tvDate -> {
                pickDateTime()
            }

            R.id.ivSend -> {
                preventDoubleClick(view)
                hideKeyboard()
                binding.llEmojis.gone()
                sendMessage()
            }

            R.id.btnSendEmoji -> {
                preventDoubleClick(view)
                /*  if (chatSoundEmojisAdapter?.selectedEmoji != null) {
                      if (binding.ivSchedule.isVisible) {
                          if (mediaList.size < 5) {
                              binding.rvScheduleMessage.isVisible = true
                              mediaList.add(
                                  MediaUris(
                                      com.chatsta.model.feed.MediaType.SOUND_EMOJI,
                                      url = chatSoundEmojisAdapter?.selectedEmoji?.imageUrl.toString(),
                                      thumbnailUrl = chatSoundEmojisAdapter?.selectedEmoji?.audioUrl.toString()
                                  )
                              )
                              mediaAdapter.notifyDataSetChanged()
                          } else {
                              showToastLong("Max Limit : 5")
                          }
                      } else {
                          sendEmojiMessage(
                              chatSoundEmojisAdapter?.selectedEmoji?.imageUrl.toString(),
                              chatSoundEmojisAdapter?.selectedEmoji?.audioUrl.toString(),
                              MessageType.SOUND_EMOJI
                          )
                      }
                  }
                  */binding.llEmojis.gone()
            }

            R.id.tvToolbarTitle -> {
                preventDoubleClick(view)
                val bundle = Bundle()
                bundle.putString("groupId", threadId)
//                bundle.putBoolean("isBlockedUser", isBlockedUser)
                findNavController().navigate(R.id.to_group_message_setting, bundle)
            }
        }
    }

    /*   private fun scheduledMessages(mediaModel: MediaUris) {
           val timeStamp = binding.tvDate.tag.toString().toLong()
           val messageId = mThreadReference?.push()?.key
           val aes = SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
           val encryptedUrl = aes.encryption(mediaModel.url.toString())
           val encryptedThumbnail =
               if (mediaModel.thumbnailUrl.isNullOrBlank()) "" else aes.encryption(mediaModel.thumbnailUrl.toString())
           val message = Message(
               messageId.toString(),
               when (mediaModel.mediaType) {
                   com.chatsta.model.feed.MediaType.VIDEO -> {
                       MessageType.VIDEO
                   }

                   com.chatsta.model.feed.MediaType.IMAGE -> {
                       MessageType.IMAGE
                   }

                   com.chatsta.model.feed.MediaType.AUDIO -> {
                       MessageType.AUDIO
                   }

                   com.chatsta.model.feed.MediaType.FILE -> {
                       MessageType.FILE
                   }

                   com.chatsta.model.feed.MediaType.EMOJI -> {
                       MessageType.EMOJI
                   }

                   com.chatsta.model.feed.MediaType.SOUND_EMOJI -> {
                       MessageType.SOUND_EMOJI
                   }

                   else -> {
                       MessageType.TEXT
                   }
               },
               MediaStatus.SUCCESS,
               session?.user?.userId.toString(),
               encryptedUrl,
               encryptedThumbnail,
               mediaModel.duration,
               timeStamp
           )
           messages.add(message)
           if (mediaList.size == messages.size) {
               schedule(messages, message.timestamp)
           }
       }
   */

    private fun recorderView() {
        binding.recordButton.setRecordView(binding.recordView)
        binding.recordView.setCounterTimeColor(requireContext().getColor(color.black))
        binding.recordView.setSoundEnabled(true)
        binding.recordButton.isListenForRecord = true
        //binding.recordView.setBackgroundResource(R.drawable.bg_border_purple)
        binding.llRecordView.setPadding(16, 0, 16, 0)
        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8
        binding.recordView.cancelBounds = 8F
        binding.recordView.setSmallMicColor(Color.parseColor("#c2185b"))
        //prevent recording under one Second
        binding.recordView.setLessThanSecondAllowed(false)
        binding.recordView.setSlideToCancelText("Slide To Cancel")
        // binding.recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0)


        binding.recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                startRecording()
                Log.d("RecordView", "onStart")
            }

            override fun onCancel() {
                stopRecording(true, 0)
                Log.d("RecordView", "onCancel")
            }

            override fun onFinish(recordTime: Long, limitReached: Boolean) {
                stopRecording(false, recordTime)
                binding.llMainActions.visible()
                binding.cvRecordView.gone()
            }

            override fun onLessThanSecond() {
                stopRecording(true, 0)
                Log.d("RecordView", "onLessThanSecond")
            }
        })


        binding.recordView.setOnBasketAnimationEndListener({
            Log.d(
                "RecordView",
                "Basket Animation Finished"
            )
            binding.llMainActions.visible()
            binding.cvRecordView.gone()
        })

        binding.recordView.setRecordPermissionHandler(RecordPermissionHandler {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return@RecordPermissionHandler true
            }
            val recordPermissionAvailable = ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
            if (recordPermissionAvailable) {
                return@RecordPermissionHandler true
            }
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO),
                0
            )
            false
        })
    }

    fun startRecording() {
        recorder = MediaRecorder()
        recorder?.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder?.setOutputFile(getFileName())
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

        try {
            recorder?.prepare()
            recorder?.start()
        } catch (e: IOException) {
            Log.e("Audio Recorder Status----", "prepare() failed--Failed to start recording")
        }
        Log.e("Audio Recorder Status----", "Started Recording")
    }

    fun stopRecording(cancel: Boolean, recordTime: Long) {
        var minutes = "00"
        var seconds = "00"
        if (recordTime != null) {
            minutes = String.format("%02d", recordTime / 1000 / 60)
            seconds = String.format("%02d", recordTime / 1000 % 60)
        }
        if (!cancel) {
            Log.e("Recorded Audio File:---", "Stopped Recording $fileName")
            uploadMedia(
                MessageType.AUDIO,
                Uri.fromFile(File(fileName.toString())),
                fileDuration = "$minutes:$seconds"
            )
        }
        recorder?.apply {
            try {
                stop()
            } catch (e: Exception) {
                Log.e("stopRecording --->", "stopRecording: ${e.message.toString()}")
            }
            release()
        }
        recorder = null
        fileName = null
    }

    fun getFileName(): String? {
        fileName = requireContext().externalCacheDir!!.absolutePath
        fileName += "/${System.nanoTime()}.m4a"
        val f = File(fileName.toString())
        if (!f.exists()) {
            f.createNewFile()
        }
        return fileName
    }

    /* private fun scheduleEmojiMessage(
         type: MessageType,
         emojiUrl: String,
         audioUrl: String? = null,
     ) {
         val messageId = mThreadReference?.push()?.key
         val aes = SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
         val encryptedMessage = aes.encryption(emojiUrl)
         val encryptedUrl = if (audioUrl.isNullOrBlank()) audioUrl else aes.encryption(audioUrl)
         val message =
             Message(
                 encryptedMessage.toString(),
                 encryptedUrl.toString(),
                 messageId.toString(),
                 session?.user?.userId.toString(),
                 timeStamp = binding.tvDate.tag.toString().toLong(),
                 type
             )
         messages.add(message)
         if (mediaList.size == messages.size) {
             schedule(messages, message.timestamp)
         }
     }
 */
    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        val datePicker = DatePickerDialog(requireContext(), { _, year, month, day ->
            val timePicker = TimePickerDialog(requireContext(), { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                if (pickedDateTime.timeInMillis < System.currentTimeMillis()) {
                    showToastLong("Date already passed")
                } else {
                    pickedDateTime.set(Calendar.SECOND, 0)
                    pickedDateTime.set(Calendar.MILLISECOND, 0)
                    binding.tvDate.tag = pickedDateTime.timeInMillis
//                    binding.tvDate.text = TimeStamp.millisToFormat(
//                        pickedDateTime.timeInMillis,
//                        TimeStamp.FULL_DATE_FORMAT_SCHEDULE
//                    )
                }
            }, startHour, startMinute, false)
            timePicker.show()
            timePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(requireContext().getColor(color.red))
            timePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(requireContext().getColor(color.purple))
        }, startYear, startMonth, startDay)
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(requireContext().getColor(color.red))
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(requireContext().getColor(color.purple))
    }

    /*private fun scheduleMessage() {
        if (binding.evMessage.text.toString().trim().isNotEmpty()) {
            val messageId = mThreadReference?.push()?.key
            val aes = SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
            val encryptedMessage = aes.encryption(binding.evMessage.text.toString())
            val message =
                Message(
                    messageId.toString(),
                    session?.user?.userId.toString(),
                    encryptedMessage,
                    timeStamp = binding.tvDate.tag.toString().toLong()
                )

            //Clearing text filed
            binding.evMessage.setText("")
            binding.tvDate.text = ""
            messages.add(message)

            schedule(messages, message.timestamp)
        }
    }
*/
//    private fun scheduleUploadMedia(
//        type: MessageType, path: Uri, pos: Long = 0, fileDuration: String,
//    ) {
//
//        val uploadUtils = AWSUtils(
//            context = requireContext(),
//            filePath = FilePathUtil.getPath(requireContext(), path).toString(),
//            onAwsImageUploadListener = object : AWSUtils.OnAwsImageUploadListener {
//
//                override fun showProgressDialog() {
////                    showToastLong("Uploading")
//                }
//
//                override fun hideProgressDialog() {
//
//                }
//
//                override fun onSuccess(mediaModel: PostMediaModel, position: Int?) {
//                    uploadedMedia.add(mediaModel)
//                    if ((uploadedMedia.size + messages.size) >= mediaList.size) {
//                        scheduleMediaMessage(uploadedMedia)
//                    }
//
//                }
//
//                override fun onError(errorMsg: String, id: String?) {
//                    showToastLong("Upload Failed $errorMsg")
////                    sendMediaMessageFailed(id)
//                }
//            },
//            filePathKey = AwsConstants.folderPath(
//                session?.user,
//                when (type) {
//                    MessageType.IMAGE -> AwsConstants.IMAGE
//                    MessageType.AUDIO -> AwsConstants.AUDIO
//                    MessageType.VIDEO -> AwsConstants.VIDEO
//                    else -> AwsConstants.FILE
//                }
//            ),
//            type = when (type) {
//                MessageType.VIDEO -> {
//                    com.chatsta.model.feed.MediaType.VIDEO
//                }
//
//                MessageType.AUDIO -> {
//                    com.chatsta.model.feed.MediaType.AUDIO
//                }
//
//                MessageType.FILE -> {
//                    com.chatsta.model.feed.MediaType.FILE
//                }
//
//                else -> {
//                    com.chatsta.model.feed.MediaType.IMAGE
//                }
//            },
//            getVideoThumbnail = type == MessageType.VIDEO,
//            duration = if (fileDuration.isNotBlank() && fileDuration != "00:00") fileDuration
//            else if (type == MessageType.VIDEO || type == MessageType.AUDIO) getVideoTime(
//                path
//            ) else "",
//        )
//
//        Handler(Looper.getMainLooper()).postDelayed({ uploadUtils.beginUpload() }, 500 * pos)
//    }

    /* private fun scheduleMediaMessage(mediaModelList: ArrayList<PostMediaModel>) {
         if (uploadedMedia.isNotEmpty()) {
             val timeStamp = binding.tvDate.tag.toString().toLong()
             for (mediaModel in mediaModelList) {
                 val messageId = mThreadReference?.push()?.key
                 val aes = SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                 val encryptedUrl = aes.encryption(mediaModel.url.toString())
                 val encryptedThumbnail =
                     if (mediaModel.thumbnailUrl.isNullOrBlank()) "" else aes.encryption(mediaModel.thumbnailUrl.toString())
                 val message = Message(
                     messageId.toString(),
                     when (mediaModel.type) {
                         Constants.VIDEO -> {
                             MessageType.VIDEO
                         }

                         Constants.IMAGE -> {
                             MessageType.IMAGE
                         }

                         Constants.AUDIO -> {
                             MessageType.AUDIO
                         }

                         Constants.FILE -> {
                             MessageType.FILE
                         }

                         else -> {
                             MessageType.TEXT
                         }
                     },
                     MediaStatus.SUCCESS,
                     session?.user?.userId.toString(),
                     encryptedUrl,
                     encryptedThumbnail,
                     mediaModel.videoDuration,
                     timeStamp
                 )
                 messages.add(message)
             }

             //Clearing text filed
             binding.evMessage.setText("")
             binding.tvDate.text = ""

             schedule(messages, timeStamp)

         }
     }
 */
    /*  private fun schedule(messageList: ArrayList<Message>, timeStamp: Long) {
          binding.llEmojis.gone()
          binding.llScheduleMessage.gone()
          binding.evMessage.setText("")
          binding.tvDate.text = ""
          binding.ivSend.visible()
          binding.ivSchedule.gone()
          binding.ivTimer.visible()
          binding.ivFire.visible()
          mediaList.clear()
          mediaAdapter.notifyDataSetChanged()

          mScheduleMessages?.child(Constants.TABLE_MESSAGES)?.setValue(messageList)
          mScheduleMessages?.child("timestamp")?.setValue(timeStamp)
  //        val intent = Intent(requireActivity(), ScheduleMessageReceiver::class.java)
  //        val bundle = Bundle()
  //        bundle.putBoolean("isGroup", true)
  //        bundle.putSerializable("messages", messageList)
  //        bundle.putString("threadId", threadId)
  //        intent.putExtra(Constants.DATA, bundle)
  //        intent.action = "${threadId}_${System.currentTimeMillis()}"
  //        val pendingIntent = PendingIntent.getBroadcast(
  //            context,
  //            100,
  //            intent,
  //            PendingIntent.FLAG_MUTABLE
  //        )
  //        val alarmManager =
  //            requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
  //        alarmManager.setExactAndAllowWhileIdle(
  //            AlarmManager.RTC,
  //            timeStamp,
  //            pendingIntent
  //        )
          messages.clear()
          viewModel.isLoading.value = false
      }
  */

    private fun openMediaPicker() {
        SelectMediaDialog.getInstance(requireContext(),
            object : SelectMediaDialog.OnMediaPickedListener {
                override fun onMediaPicked(dialog: SelectMediaDialog, mediaType: MessageType) {
                    dialog.dismiss()
                    when (mediaType) {
                        MessageType.MEDIA -> {
                            MediaPickerBottomSheetDialog.newInstance(
                                this@GroupChatFragment,
                                true
                            ).show(parentFragmentManager, "PICK MEDIA")
                        }

                        MessageType.AUDIO -> {
                            pickAudio()
                        }

                        MessageType.FILE -> {
                            pickPdf()
                        }

                        else -> {}
                    }
                }
            }).show()
    }

    override fun onMediaPicked(type: MediaType, uris: List<Uri>) {
        when (type) {
            MediaType.VIDEO -> {
                for (i in uris.indices) {
                    val size = FileUtils(requireContext()).getFileSize(uris[i])
                    if (size != null && size <= 30000000) {
                        uploadMedia(MessageType.VIDEO, uris[i], i.toLong())
                    } else {
                        showToastShort(getString(string.ett_file_size))
                    }
                }
            }

            else -> {
                for (pos in uris.indices) {
                    uploadMedia(MessageType.IMAGE, uris[pos], pos.toLong())
                }
            }
        }

        /*uploadMedia(mediaType,uris)*/
    }

    fun getVideoTime(uri: Uri): String {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val timeInMilliSec = time?.toLong()
        var minutes = "00"
        var seconds = "00"
        if (timeInMilliSec != null) {
            minutes = String.format("%02d", timeInMilliSec / 1000 / 60)
            seconds = String.format("%02d", timeInMilliSec / 1000 % 60)
        }
        retriever.release()
        return "$minutes:$seconds"
    }

    private val filesResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    if (result.data?.clipData != null) {
                        if (result.data?.clipData!!.itemCount > 5) {
                            showToastLong("Max Limit : 5")
                        } else {
                            for (i in 0 until result.data?.clipData!!.itemCount) {
                                val uri = result.data?.clipData?.getItemAt(i)?.uri
                                if (uri != null) {
                                    val size = uri.let {
                                        FilePathUtil.getFileSize(
                                            requireContext(),
                                            it
                                        )
                                    }
                                    if (size != null && size <= 30000000) {
                                        uploadMedia(MessageType.FILE, uri, i.toLong())
                                    } else {
                                        showToastShort(getString(string.err_cv_file_size))
                                    }
                                }
                            }
                        }
                    } else {
                        val size = result.data?.data?.let {
                            FilePathUtil.getFileSize(
                                requireContext(),
                                it
                            )
                        }
                        if (size != null && size <= 30000000) {
                            uploadMedia(MessageType.FILE, result.data?.data!!)
                        } else {
                            showToastShort(getString(string.err_cv_file_size))
                        }
                    }
                }
            }
        }

    private val audioResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    if (result.data?.clipData != null) {
                        if (result.data?.clipData!!.itemCount > 5) {
                            showToastLong("Max Limit : 5")
                        } else {
                            for (i in 0 until result.data?.clipData!!.itemCount) {
                                val uri = result.data?.clipData?.getItemAt(i)?.uri
                                if (uri != null) {
                                    val size = uri.let {
                                        FilePathUtil.getFileSize(
                                            requireContext(),
                                            it
                                        )
                                    }
                                    if (size != null && size <= 30000000) {
                                        uploadMedia(MessageType.AUDIO, uri, i.toLong())
                                    } else {
                                        showToastShort(getString(string.err_cv_file_size))
                                    }
                                }
                            }
                        }
                    } else {
                        val size = result.data?.data?.let {
                            FilePathUtil.getFileSize(
                                requireContext(),
                                it
                            )
                        }
                        if (size != null && size <= 30000000) {
                            uploadMedia(MessageType.AUDIO, result.data?.data!!)
                        } else {
                            showToastShort(getString(string.err_cv_file_size))
                        }
                    }
                }
            }
        }

    fun pickPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        pdfIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        filesResult.launch(pdfIntent)
    }

    fun pickAudio() {
        val audioIntent = Intent(Intent.ACTION_GET_CONTENT)
        audioIntent.type = "audio/*"
        audioIntent.addCategory(Intent.CATEGORY_OPENABLE)
        audioIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        audioResult.launch(audioIntent)
    }


    private fun uploadMedia(
        type: MessageType, path: Uri, pos: Long = 0, fileDuration: String = "00:00",
    ) {
        if (binding.ivSchedule.isVisible) {
            binding.rvScheduleMessage.visible()
            /* if (mediaList.size < 5) {
                 mediaList.add(
                     MediaUris(
                         when (type) {
                             MessageType.VIDEO -> {
                                 com.chatsta.model.feed.MediaType.VIDEO
                             }

                             MessageType.AUDIO -> {
                                 com.chatsta.model.feed.MediaType.AUDIO
                             }

                             MessageType.FILE -> {
                                 com.chatsta.model.feed.MediaType.FILE
                             }

                             else -> {
                                 com.chatsta.model.feed.MediaType.IMAGE
                             }
                         },
                         uri = path,
                         duration = fileDuration
                     )
                 )
                 mediaAdapter.notifyDataSetChanged()
             } else {
                 showToastLong("Max Limit : 5")
             }*/
        }
        /*        else {
                    val messageId = mThreadReference?.push()?.key
                    val aes = SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
                    val encryptedUri = aes.encryption(path.toString())

                    val message = Message(
                        messageId.toString(),
                        type,
                        MediaStatus.PROGRESS,
                        session?.user?.userId.toString(),
                        encryptedUri,
                        "",
                        fileDuration
                    )

                    mThreadReference?.child(messageId.toString())?.setValue(message)
                    mThreadReference?.child(messageId.toString())?.child("readBy")
                        ?.updateChildren(mapOf(session?.user?.userId to true))

                    val uploadUtils = AWSUtils(
                        context = requireContext(),
                        filePath = FilePathUtil.getPath(requireContext(), path).toString(),
                        onAwsImageUploadListener = object : AWSUtils.OnAwsImageUploadListener {

                            override fun showProgressDialog() {
        //                    showToastLong("Uploading")
                            }

                            override fun hideProgressDialog() {

                            }

                            override fun onSuccess(mediaModel: PostMediaModel, position: Int?) {
                                sendMediaMessage(mediaModel)
                            }

                            override fun onError(errorMsg: String, id: String?) {
                                showToastLong("Upload Failed $errorMsg")
                                sendMediaMessageFailed(id)
                            }
                        },
                        filePathKey = AwsConstants.folderPath(
                            session?.user,
                            when (type) {
                                MessageType.IMAGE -> AwsConstants.IMAGE
                                MessageType.AUDIO -> AwsConstants.AUDIO
                                MessageType.VIDEO -> AwsConstants.VIDEO
                                else -> AwsConstants.FILE
                            }
                        ),
                        type = when (type) {
                            MessageType.VIDEO -> {
                                com.chatsta.model.feed.MediaType.VIDEO
                            }

                            MessageType.AUDIO -> {
                                com.chatsta.model.feed.MediaType.AUDIO
                            }

                            MessageType.FILE -> {
                                com.chatsta.model.feed.MediaType.FILE
                            }

                            else -> {
                                com.chatsta.model.feed.MediaType.IMAGE
                            }
                        },
                        getVideoThumbnail = type == MessageType.VIDEO,
                        duration = if (fileDuration.isNotBlank() && fileDuration != "00:00") fileDuration
                        else if (type == MessageType.VIDEO || type == MessageType.AUDIO) getVideoTime(
                            path
                        ) else "",
                        msgId = messageId
                    )

                    Handler(Looper.getMainLooper()).postDelayed({ uploadUtils.beginUpload() }, 500 * pos)
                }*/
    }

}