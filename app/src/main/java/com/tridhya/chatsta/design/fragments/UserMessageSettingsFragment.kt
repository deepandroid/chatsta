package com.tridhya.chatsta.design.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.FragmentUserMessageSettingsBinding
import com.tridhya.chatsta.design.dialogs.SelectDestructChatTimeDialog
import com.tridhya.chatsta.design.viewModel.chat.ChatViewModel
import com.tridhya.chatsta.enum.chat.UserStatus
import com.tridhya.chatsta.provider.Constants.SELF_DESTRUCT_OFF

class UserMessageSettingsFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentUserMessageSettingsBinding
    private val viewModel by lazy { ChatViewModel(requireContext()) }
    private var userId: String? = null
    private var threadId: String? = null

    //    private var conversation: DatabaseReference? = null
    private var lastSeen: Long = 0
    private var lastSeenInChat: Long = 0
    private var isOnline: UserStatus = UserStatus.OFFLINE
    private var userSetStatus: UserStatus = UserStatus.ONLINE

    //    private var mDBReference: DatabaseReference? = null
    private var isBlockedUser = false
    private var isChatDeleted = false
//    private var eventListener: ValueEventListener? = null
//    private var userStatusEventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentUserMessageSettingsBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            val arguments = arguments
            if (arguments != null) {
                userId = arguments.getString("userId")
                isBlockedUser = arguments.getBoolean("isBlockedUser")
            }
            initViews()
        }
        return binding.root
    }

    private fun initListeners() {
/*        eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val values = dataSnapshot.value as java.util.HashMap<*, *>?
                Log.e("DataSnapshot", dataSnapshot.value.toString())

                if (values != null) {
                    if (values.containsKey("lastSeen") && values["lastSeen"] != null)
                        lastSeen = values["lastSeen"] as Long
                    if (values.containsKey("isOnline") && values["isOnline"] != null)
                        isOnline = UserStatus.valueOf(values["isOnline"] as String)
                    userSetStatus =
                        if (values.containsKey("userSetStatus")) UserStatus.valueOf(values["userSetStatus"].toString()) else UserStatus.ONLINE

                    val inChatWith = if (values.containsKey("inChatWith")) {
                        values["inChatWith"].toString()
                    } else ""

                    binding.tvLastSeenTime.text =
                        "Last seen online " + TimeStamp.getPastDayTime(lastSeen) + " at " + TimeStamp.millisToFormat(
                            lastSeen,
                            "hh:mm a"
                        )

                    if (userSetStatus == UserStatus.ONLINE) {
                        val status = if (inChatWith.isNotBlank()) {
                            if (getMessageIdChatWithMe(userId.toString()) == inChatWith) {
                                isOnline
                            } else {
                                UserStatus.BUSY
                            }
                        } else {
                            isOnline
                        }
                        when (status) {
                            UserStatus.ONLINE -> {
                                binding.tvLastSeenTime.text =
                                    (requireContext() as BaseActivity).getString(R.string.online_now)
                                binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_green)
                            }

                            UserStatus.BUSY -> {
                                binding.tvLastSeenTime.text =
                                    (requireContext() as BaseActivity).getString(R.string.online_now)
                                binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_blue)
                            }

                            else -> {
                                binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_red)
                            }
                        }
                    } else {
                        when (userSetStatus) {
                            UserStatus.ONLINE -> {
                                binding.tvLastSeenTime.text =
                                    (context as BaseActivity).getString(R.string.online_now)
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
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        userStatusEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    binding.tvLastOnlineInChat.visible()
                    val values = dataSnapshot.value as Long
                    Log.e("DataSnapshot", dataSnapshot.value.toString())
                    lastSeenInChat = values
                    binding.tvLastOnlineInChat.text =
                        "Last seen in chat " + TimeStamp.getPastDayTime(lastSeenInChat) + " at " + TimeStamp.millisToFormat(
                            lastSeenInChat,
                            "hh:mm a"
                        )
                } else {
                    binding.tvLastOnlineInChat.gone()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }*/
    }

    private fun initViews() {
        initListeners()
        checkChatUserBlockStatus(isBlockedUser)

/*        mDBReference = AppClass.mDBReference
        mDBReference?.child(Constants.TABLE_USERS)?.child(userId.toString())
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val values = dataSnapshot.value as HashMap<*, *>?
                    binding.tvUserName.text = values?.get("name").toString()
                    if (values?.containsKey("userTextStatus") == true && values["userTextStatus"] != null && values["userTextStatus"].toString()
                            .isNotBlank()
                    ) {
                        binding.clUserStatus.visible()
                        binding.tvUserStatus.text = values["userTextStatus"].toString()
                    } else {
                        binding.clUserStatus.gone()
                    }
                    if (values?.containsKey("image") == true && values["image"].toString()
                            .isNotEmpty() && values["image"]
                            .toString().isNotBlank()
                    )
                        GlideUtils(requireContext()).circleImage()
                            .loadImage(values["image"].toString(), binding.ivProfileImage)
                    else
                        GlideUtils(requireContext()).circleImage()
                            .loadImage(R.drawable.ic_app, binding.ivProfileImage)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }

            })

        if (eventListener != null)
            mDBReference //Observing user
                ?.child(Constants.TABLE_USERS)?.child(userId.toString())
                ?.addValueEventListener(eventListener!!)

        userStatusEventListener?.let {
            mDBReference
                ?.child(Constants.TABLE_CHAT)
                ?.child(Constants.TABLE_CONVERSATIONS)
                ?.child(userId.toString())
                ?.child(threadId.toString())
                ?.child("lastOnlineInChat")
                ?.addValueEventListener(it)
        }

        threadId = mDBReference
            ?.child(Constants.TABLE_CHAT)
            ?.child(Constants.TABLE_THREADS)
            ?.child(getMessageIdChatWithMe(userId.toString()))
            ?.key

        conversation = mDBReference
            ?.child(Constants.TABLE_CHAT)
            ?.child(Constants.TABLE_CONVERSATIONS)
            ?.child(session?.user?.userId.toString())//Receiver
            ?.child(threadId.toString())

        conversation?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val values = dataSnapshot.value as java.util.HashMap<*, *>?
                    if (values != null) {
                        if (values.containsKey("isChatMuted") && values["isChatMuted"] != null && values["isChatMuted"] as Boolean) {
                            binding.tvMute.text =
                                (context as BaseActivity).getString(R.string.unmute)
                            binding.ivMute.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_mute_notifications
                                )
                            )
                        } else {
                            binding.tvMute.text = (context as BaseActivity).getString(R.string.mute)
                            binding.ivMute.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_bell_bordered
                                )
                            )
                        }
                        if (values.containsKey("selfDestruct") && values["selfDestruct"] != null && values["selfDestruct"] != Constants.SELF_DESTRUCT_OFF) {
                            binding.ivDestruct.setImageResource(R.drawable.ic_fire_red_bordered)
                            binding.tvDestruct.text = values["selfDestruct"].toString()
                            binding.tvDestruct.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.red
                                )
                            )
                        } else {
                            binding.ivDestruct.setImageResource(R.drawable.ic_fire_purple_bordered)
                            binding.tvDestruct.text =
                                (context as BaseActivity).getString(R.string.destruct)
                            binding.tvDestruct.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.pale_purple
                                )
                            )
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToastLong(error.message)
            }
        })*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        binding.ivBack.setOnClickListener(this)
        binding.llDestruct.setOnClickListener(this)
        binding.llMute.setOnClickListener(this)
        binding.llBlock.setOnClickListener(this)
        binding.tvBlockContact.setOnClickListener(this)
        binding.tvDeleteThisChat.setOnClickListener(this)
        binding.llProfileImage.setOnClickListener(this)
        binding.tvUserName.setOnClickListener(this)
    }

    private fun setObserver() {
        viewModel.responseBlockUnblockConnection.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            isBlockedUser = !isBlockedUser
            checkChatUserBlockStatus(isBlockedUser)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> {
                preventDoubleClick(view)
                findNavController().popBackStack()
            }

            R.id.llDestruct -> {
                preventDoubleClick(view)
                SelectDestructChatTimeDialog.getInstance(
                    requireContext(),
                    binding.tvDestruct.text.toString()
                ).setListener(object :
                    SelectDestructChatTimeDialog.ButtonListener {
                    override fun onDurationSelected(
                        dialog: SelectDestructChatTimeDialog,
                        duration: String,
                    ) {
                        dialog.dismiss()
                        if (duration != (context as ActivityBase).getString(R.string.off)) {
                            binding.ivDestruct.setImageResource(R.drawable.ic_fire_red_bordered)
                            binding.tvDestruct.text = duration
                            binding.tvDestruct.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.red
                                )
                            )
                            val params: MutableMap<String, Any> = java.util.HashMap()
                            params["selfDestruct"] = binding.tvDestruct.text
//                            conversation?.updateChildren(params)
                        } else if (duration == (context as ActivityBase).getString(R.string.off)) {
                            binding.ivDestruct.setImageResource(R.drawable.ic_fire_purple_bordered)
                            binding.tvDestruct.text =
                                (context as ActivityBase).getString(R.string.destruct)
                            binding.tvDestruct.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.pale_purple
                                )
                            )
                            val params: MutableMap<String, Any> = java.util.HashMap()
                            params["selfDestruct"] = SELF_DESTRUCT_OFF
//                            conversation?.updateChildren(params)
                        }
                    }
                }).show()
            }

            R.id.llMute -> {
                preventDoubleClick(view)
                checkChatMuteStatus()
            }

            R.id.llBlock, R.id.tvBlockContact -> {
                preventDoubleClick(view)
//                viewModel.isLoading.value = true
//                userId?.let { viewModel.blockUnblockConnection(userId = it) }
            }

            R.id.tvDeleteThisChat -> {
                isChatDeleted = true
                preventDoubleClick(view)
                showDeleteDialog()
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
        }
    }

    private fun showDeleteDialog() {
        val deleteDialog = Dialog(requireContext(), R.style.DefaultDeleteThemeDialog)
        deleteDialog.setContentView(R.layout.dialog_delete_account)
        deleteDialog.show()
        val title = deleteDialog.findViewById<TextView>(R.id.tvTitle)
        val msg = deleteDialog.findViewById<TextView>(R.id.tvMessage)
        val btnCancel = deleteDialog.findViewById<TextView>(R.id.btnCancel)
        val btnOk = deleteDialog.findViewById<TextView>(R.id.btnOk)
        val ivIcon = deleteDialog.findViewById<ImageView>(R.id.ivIcon)
        btnCancel.text = getString(R.string.cancel)
        btnOk.text = getString(R.string.delete)
        title.text = getString(R.string.please_note)
        ivIcon.setImageResource(R.drawable.ic_delete_rounded_red)
        msg.text = getString(R.string.are_you_sure_you_want_to_delete_chat)
        btnOk.setOnClickListener {
            deleteDialog.dismiss()
            val params: MutableMap<String, Any> = HashMap()
            params["isDeleting"] = true
            /*mDBReference?.child(Constants.TABLE_CHAT)
                ?.child(Constants.TABLE_THREADS)
                ?.child(threadId.toString())
                ?.updateChildren(params)*/
//            findNavController().previousBackStackEntry!!
//                .savedStateHandle.set<Boolean>("chatDeleted", isChatDeleted)
            findNavController().navigateUp()
        }
        btnCancel.setOnClickListener {
            deleteDialog.dismiss()
        }
    }

    private fun checkChatUserBlockStatus(isBlocked: Boolean) {
        val params: MutableMap<String, Any> = java.util.HashMap()
        params["blockedBy_${session?.user?.userId}"] = isBlocked

       /* mDBReference
            ?.child(Constants.TABLE_CHAT)
            ?.child(Constants.TABLE_THREADS)
            ?.child(threadId.toString())
            ?.updateChildren(params)
*/
        if (isBlocked) {
            binding.ivBlock.setImageResource(R.drawable.ic_unlock_red)
            binding.tvBlock.text = getString(R.string.unblock)
            binding.tvBlockContact.text =
                (context as ActivityBase).getString(R.string.unblock_this_contact)
            binding.tvBlockContact.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
            binding.tvBlock.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        } else {
            binding.ivBlock.setImageResource(R.drawable.ic_blocked)
            binding.tvBlock.text = getString(R.string.block)
            binding.tvBlockContact.text =
                (context as ActivityBase).getString(R.string.block_this_contact)
            binding.tvBlockContact.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pale_purple
                )
            )
            binding.tvBlock.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pale_purple
                )
            )
        }
    }

    private fun checkChatMuteStatus() {
        val params: MutableMap<String, Any> = java.util.HashMap()
        if (binding.tvMute.text == getString(R.string.mute)) {
            binding.tvMute.text = (context as ActivityBase).getString(R.string.unmute)
            binding.ivMute.setImageResource(R.drawable.ic_mute_notifications)
            params["isChatMuted"] = true
        } else {
            binding.tvMute.text = (context as ActivityBase).getString(R.string.mute)
            binding.ivMute.setImageResource(R.drawable.ic_bell_bordered)
            params["isChatMuted"] = false
        }
//        conversation?.updateChildren(params)
    }

    override fun onStop() {
        super.onStop()
       /* if (eventListener != null) {
            mDBReference //Observing user
                ?.child(Constants.TABLE_USERS)?.child(userId.toString())
                ?.removeEventListener(eventListener!!)
        }*/
    }

}