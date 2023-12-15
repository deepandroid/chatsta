package com.tridhya.chatsta.design.fragments.chat

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
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
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.FragmentChatBinding
import com.tridhya.chatsta.design.adapters.ChatAdapter
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.chat.ChatViewModel
import com.tridhya.chatsta.enum.chat.MessageType
import com.tridhya.chatsta.enum.chat.UserStatus
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.Message
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.provider.Constants.SELF_DESTRUCT_OFF
import com.tridhya.chatsta.provider.dummyData.chat.getMessages.getChatMessages
import com.tridhya.chatsta.utils.FileUtils
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.MyEditText
import java.io.File
import java.io.IOException
import java.util.Calendar


class ChatFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentChatBinding
    private val viewModel by lazy { ChatViewModel(requireContext()) }
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    private var adapter: ChatAdapter? = null

    private var selectedUserId: String? = null

    val messages = arrayListOf<Message>()


    private var userId: String? = null
    private var isBlockedUser = false
    private var isBlockedForChat = false
    private var isDeletedUser = false
    private var userName: String? = null
    private var threadId: String? = null

    private var lastSeen: Long = 0
    private var isOnline: UserStatus = UserStatus.OFFLINE
    private var userSetStatus: UserStatus = UserStatus.ONLINE

    private var scheduledMessages: ArrayList<Message> = arrayListOf()
    private var tempMessageModel: Message? = null
//    private var tempReactionModel: PostReactionsResponseModel? = null

    private var isChatDeleted = false

    private var recorder: MediaRecorder? = null
    private var fileName: String? = null

    private var destructDuration: String? = SELF_DESTRUCT_OFF
    private var selfDestructTime = SELF_DESTRUCT_OFF

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
        resetUnreadCount()
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        val arguments = arguments
        if (arguments != null) {
            userId = arguments.getString("userId")
        }
//        AppClass.setInChat(userId.toString(), true)

//        viewModel.isLoading.value = true
//        viewModel.getUserProfile(userId)
        binding.pbEmojis.visible()
        return binding.root
    }


    private fun recorderView() {
        binding.recordButton.setRecordView(binding.recordView)
        binding.recordView.setCounterTimeColor(requireContext().getColor(R.color.black))
        binding.recordView.setSoundEnabled(true)
        binding.recordButton.isListenForRecord = true
        binding.recordView.setBackgroundResource(R.drawable.bg_border_purple)
        binding.llRecordView.setPadding(16, 0, 16, 0)
        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8
        binding.recordView.cancelBounds = 8F
        binding.recordView.setSmallMicColor(Color.parseColor("#c2185b"))

        //prevent recording under one Second
        binding.recordView.setLessThanSecondAllowed(false)
        binding.recordView.setSlideToCancelText("Slide To Cancel")
//        binding.recordButton.isListenForRecord = false
        /* binding.recordButton.setOnRecordClickListener(OnRecordClickListener {
             Toast.makeText(requireContext(), "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show()
             Log.d("RecordButton", "RECORD BUTTON CLICKED")
         })*/


//        binding.recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0)
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

        binding.recordView.setOnBasketAnimationEndListener {
            Log.d(
                "RecordView",
                "Basket Animation Finished"
            )
            binding.llMainActions.visible()
            binding.cvRecordView.gone()
        }

        binding.recordView.setRecordPermissionHandler(RecordPermissionHandler {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return@RecordPermissionHandler true
            }
            val recordPermissionAvailable = ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) == PERMISSION_GRANTED
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName = session?.user?.username
        binding.tvUserName.text = userName
        binding.tvToolbarTitle.text = userName
        isDeletedUser = false
        binding.llBlock.gone()
        binding.llMainActions.visible()

        binding.clUserStatus.visible()
        binding.clToolbarUserStatus.visible()
        binding.tvUserStatus.text = "Focusing on goals"
        binding.tvToolbarUserStatus.text = "Focusing on goals"

        GlideUtils(requireContext()).circleImage()
            .loadImage("https://source.unsplash.com/user/c_v_r/1900x800", binding.ivProfileImage)

        binding.rvScheduleMessage.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        initViews()
        setObservers()
        binding.ivClose.setOnClickListener(this)
        binding.ivAddMedia.setOnClickListener(this)
        binding.ivMicrophone.setOnClickListener(this)
        binding.ivEmoji.setOnClickListener(this)
        binding.ivTimer.setOnClickListener(this)
        binding.tvDelete.setOnClickListener(this)
        binding.tvDate.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)
        binding.ivFire.setOnClickListener(this)
        binding.ivSend.setOnClickListener(this)
        binding.btnSendEmoji.setOnClickListener(this)
        binding.tvToolbarTitle.setOnClickListener(this)
        binding.ivSchedule.setOnClickListener(this)
        binding.ivDeleteAudio.setOnClickListener(this)
        binding.llProfileImage.setOnClickListener(this)
        binding.tvUserName.setOnClickListener(this)

        binding.evMessage.setOnClickListener {
            preventDoubleClick(it)
            binding.llEmojis.gone()
        }
    }

    private fun setObservers() {
        viewModel.responseUserData.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            isBlockedUser = (it.isBlockedConnection == Constants.BLOCK_USER)
            isBlockedForChat = it.isBlockedForChat == true

            val params: MutableMap<String, Any> = HashMap()
            params["blockedBy_${session?.user?.userId}"] = isBlockedUser
            params["blockedBy_${userId}"] = isBlockedForChat


            if (!isDeletedUser) {
                when {
                    isBlockedUser -> {
                        binding.llBlock.visible()
                        binding.llMainActions.gone()
                        binding.tvBlockContact.text = getString(R.string.you_have_blocked_this_user)
                    }

                    isBlockedForChat -> {
                        binding.llBlock.visible()
                        binding.llMainActions.gone()
                        binding.tvBlockContact.text =
                            getString(R.string.you_can_no_longer_reply_to_this_conversation)
                    }

                    else -> {
                        binding.llBlock.gone()
                        binding.llMainActions.visible()
                    }
                }
            } else {
                binding.llBlock.visible()
                binding.llMainActions.gone()
                binding.tvBlockContact.text = getString(R.string.user_has_deleted_account)
            }
        }

    }

    private fun initViews() {
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
                            val uri = inputContentInfo.contentUri
                            val size = FileUtils(requireContext()).getFileSize(uri)
                            if (size != null && size <= 30000000) {
                                uploadMedia(MessageType.IMAGE, uri, 1)
                            } else {
                                showToastShort(getString(R.string.ett_file_size))
                            }
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

    override fun onResume() {
        super.onResume()
        (context as ActivityBase).disableScreenShots()
        binding.rvMessages.post {
            Handler(Looper.getMainLooper()).postDelayed(
                { scrollDown() },
                100
            )
        }

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose -> {
                preventDoubleClick(view)
                findNavController().navigateUp()
            }

            R.id.llProfileImage, R.id.tvUserName -> {
                if (session?.user?.userId == userId) {
                    findNavController().navigate(R.id.profile)
                } else {
                    val bundle = Bundle()
                    bundle.putString("userId", userId)
                    findNavController().navigate(R.id.to_user_profile, bundle)
                }
            }

            R.id.ivDeleteAudio -> {
                preventDoubleClick(view)
                if (binding.cvRecordView.isVisible) {
                    binding.cvRecordView.gone()
//                    if (recorder != null) {
//                        stopRecording(true, 0)
//                    }
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
                    ) != PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.RECORD_AUDIO), Constants.RECORD_AUDIO
                    )
                } else {
                    binding.llMainActions.gone()
                    binding.cvRecordView.visible()
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
            }

            R.id.tvDelete -> {
                preventDoubleClick(view)
                hideKeyboard()
                MessageDialog.getInstance(
                    requireContext(),
                    getString(R.string.delete_schedule_msg_txt),
                    R.style.ErrorThemeDialog
                )
                    .setPositiveButtonText(R.string.delete)
                    .setNegativeButton(getString(R.string.cancel))
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
                    getString(R.string.cancel_schedule_msg_txt),
                    R.style.DefaultThemeDialog
                )
                    .setPositiveButtonText(R.string.continue_txt)
                    .setNegativeButton(getString(R.string.cancel))
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


                            MessageType.TEXT -> {
                                binding.evMessage.setText(it.message)
                            }

                            else -> {}
                        }
                    }
//                    mediaAdapter.notifyDataSetChanged()
                }
            }

            R.id.ivSchedule -> {
                preventDoubleClick(view)
                hideKeyboard()
                /*if (binding.tvDate.tag != null) {
                    if (mediaList.isNotEmpty() || binding.evMessage.text.toString().trim()
                            .isNotEmpty() || binding.evMessage.text.toString().trim()
                            .isNotBlank()
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
                                                MediaType1.IMAGE -> MessageType.IMAGE
                                                MediaType1.VIDEO -> MessageType.VIDEO
                                                MediaType1.AUDIO -> MessageType.AUDIO
                                                MediaType1.FILE -> MessageType.FILE
                                                else -> MessageType.TEXT
                                            }, it, i.toLong(), mediaList[i].duration.toString()
                                        )
                                    }
                                } else if (!mediaList[i].url.isNullOrBlank()) {
                                    if (mediaList[i].mediaType != null && !(mediaList[i].mediaType == MediaType1.SOUND_EMOJI || mediaList[i].mediaType == MediaType1.EMOJI)) {
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
                        showToastShort(getString(R.string.err_create_post))
                    }
                } else {
                    showToastShort(getString(R.string.err_select_schedule_time))
                }*/
            }

            R.id.tvDate -> {
                preventDoubleClick(view)
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
                /*                if (chatSoundEmojisAdapter?.selectedEmoji != null) {
                                    if (binding.ivSchedule.isVisible) {
                                        if (mediaList.size < 5) {
                                            binding.rvScheduleMessage.isVisible = true
                                            mediaList.add(
                                                MediaUris(
                                                    MediaType1.SOUND_EMOJI,
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
                                }*/
                binding.llEmojis.gone()
            }

            R.id.tvToolbarTitle -> {
                preventDoubleClick(view)
                val bundle = Bundle()
                bundle.putString("userId", userId)
                bundle.putBoolean("isBlockedUser", isBlockedUser)
                findNavController().navigate(R.id.to_user_message_setting, bundle)
            }
        }
    }

    private fun checkInternetAndInitialize() {

        val params: MutableMap<String, Any> = HashMap()
        params["inChatWith"] = getMessageIdChatWithMe(userId.toString())

        val mLayoutManager = LinearLayoutManager(requireContext())
        mLayoutManager.stackFromEnd = true
        binding.rvMessages.layoutManager = mLayoutManager
        binding.rvMessages.itemAnimator = null

        binding.evMessage.doOnTextChanged { text, start, before, count ->
            updateUserStatus(true)
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(runnable, 1500)
        }
        updateUserStatus(true)

        if (adapter == null || adapter?.itemCount == 0) {

            val chatMessages = getChatMessages()
            adapter = ChatAdapter(chatMessages)
            binding.rvMessages.adapter = adapter

            binding.rvMessages.post {
                Handler(Looper.getMainLooper()).postDelayed(
                    { scrollDown() },
                    100
                )
            }
        }
        getSelfDestruct()
        setChatHistoryAdapter()
        checkBlockedUser()
        checkDeletedChat()
    }

    private fun checkDeletedChat() {

    }

    private fun checkBlockedUser() {

    }

    private fun setChatHistoryAdapter() {

    }

    val handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = Runnable {
        updateUserStatus(false)
    }

    private fun updateUserStatus(isTyping: Boolean) {
        val myUserId = session?.user?.userId.toString()
        if (userSetStatus == UserStatus.ONLINE) {
            val status = UserStatus.BUSY
            when (status) {
                UserStatus.ONLINE -> {
                    binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_green)
                    binding.ivIndicator.setImageResource(R.drawable.v_ic_circle_green)
                }

                UserStatus.BUSY -> {
                    binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_blue)
                    binding.ivIndicator.setImageResource(R.drawable.v_ic_circle_blue)
                }

                else -> {
                    binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_red)
                    binding.ivIndicator.setImageResource(R.drawable.v_ic_circle_red)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.clearFlags(
            WindowManager.LayoutParams.FLAG_SECURE
        )
        handler.removeCallbacks(runnable)
    }

    private fun resetUnreadCount() {
        //Reset read count
    }

    private fun sendMessage() {
        if (binding.evMessage.text.toString().trim().isNotEmpty()) {

            //Clearing text filed
            binding.evMessage.setText("")
        }
    }

    private fun getSelfDestruct() {
    }

    private fun scheduleMessage() {

    }

    private fun scrollDown() {
        if (adapter != null) {
            val viewHolder =
                binding.rvMessages.findViewHolderForAdapterPosition(adapter!!.itemCount - 1)
            viewHolder?.itemView?.y?.toInt()?.let {
                binding.nestedScrollView.smoothScrollTo(
                    0,
                    it
                )
            }
        }
    }

    private fun sendEmojiMessage(emojiUrl: String, audioUrl: String = "", type: MessageType) {
        //Clearing text filed
        binding.evMessage.setText("")
        val threadRef = getMessageIdChatWithMe(userId.toString())

    }

    private fun iterateUploadMedia(list: List<Message>, count: Int = 0) {
        if (list.size >= count) return
//        val message = list[count]
    }

    private fun uploadMedia(
        type: MessageType,
        path: Uri,
        pos: Long = 0,
        fileDuration: String = "00:00",
    ) {

        if (binding.ivSchedule.isVisible) {
            binding.rvScheduleMessage.visible()
            showToastLong("Max Limit : 5")
        }
    }

    private fun openMediaPicker() {
    }

    private fun getVideoTime(uri: Uri): String {
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

                                    showToastShort(getString(R.string.err_cv_file_size))
                                }
                            }
                        }
                    } else {
                        showToastShort(getString(R.string.err_cv_file_size))
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
                                    /* val size = uri.let {
                                         FilePathUtil.getFileSize(
                                             requireContext(),
                                             it
                                         )
                                     }
                                     if (size != null && size <= 30000000) {
                                         uploadMedia(MessageType.AUDIO, uri, i.toLong())
                                     } else {
                                         showToastShort(getString(R.string.err_cv_file_size))
                                     }*/
                                }
                            }
                        }
                    } else {
                        /*val size = result.data?.data?.let {
                            FilePathUtil.getFileSize(
                                requireContext(),
                                it
                            )
                        }
                        if (size != null && size <= 30000000) {
                            uploadMedia(MessageType.AUDIO, result.data?.data!!)
                        } else {*/
                        showToastShort(getString(R.string.err_cv_file_size))
//                        }
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

    private fun deleteChat() {
        isChatDeleted = true
    }

    private fun deleteChatAnimation() {
//        (requireActivity() as ActivityBase).showGIF(R.raw.fire_flood_animation)
        Handler(Looper.getMainLooper()).postDelayed({
            deleteChat()
            findNavController().navigateUp()
        }, 3800)
    }

    private fun updateLastOnline() {

        val lastOnlineInChatParams: MutableMap<String, Any> = HashMap()
        lastOnlineInChatParams["lastOnlineInChat"] = System.currentTimeMillis()
//        mDBReference
//            ?.child(Constants.TABLE_CHAT)
//            ?.child(Constants.TABLE_CONVERSATIONS)
//            ?.child(session?.user?.userId.toString())
//            ?.child(threadId.toString())
//            ?.updateChildren(lastOnlineInChatParams)
    }

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
                    /*binding.tvDate.text = TimeStamp.millisToFormat(
                        pickedDateTime.timeInMillis,
                        TimeStamp.FULL_DATE_FORMAT_SCHEDULE
                    )*/
                }
            }, startHour, startMinute, false)
            timePicker.show()
            timePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(requireContext().getColor(R.color.red))
            timePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(requireContext().getColor(R.color.purple))
        }, startYear, startMonth, startDay)
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(requireContext().getColor(R.color.red))
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(requireContext().getColor(R.color.purple))
    }

    fun startRecording() {
        recorder = MediaRecorder()
        recorder?.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder?.setOutputFile(getFileName())
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        recorder?.setAudioEncodingBitRate(16 * 44100)
        recorder?.setAudioSamplingRate(44100)

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

    private fun getFileName(): String? {
        fileName = requireContext().externalCacheDir!!.absolutePath
        fileName += "/${System.nanoTime()}.m4a"
        val f = File(fileName.toString())
        if (!f.exists()) {
            f.createNewFile()
        }
        return fileName
    }
}