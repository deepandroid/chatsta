package com.tridhya.chatsta.design.fragments.chat

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.FragmentGroupMessageSettingsBinding
import com.tridhya.chatsta.databinding.FragmentGroupMessageSettingsBinding.inflate
import com.tridhya.chatsta.design.dialogs.GroupMemberInfoDialog
import com.tridhya.chatsta.design.dialogs.SelectDestructChatTimeDialog
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.chat.ChatViewModel
import com.tridhya.chatsta.enum.chat.UserStatus
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.provider.Constants.SELF_DESTRUCT_OFF
import java.lang.reflect.Member

class GroupMessageSettingsFragment : BaseFragment(), View.OnClickListener,
    GroupMemberInfoDialog.ButtonListener {

    private lateinit var binding: FragmentGroupMessageSettingsBinding
    private val viewModel by lazy { ChatViewModel(requireContext()) }
    private var groupId: String? = null

    //    private var conversation: DatabaseReference? = null
    private var lastSeen: Long = 0
    private var isOnline: UserStatus = UserStatus.OFFLINE
    private var userSetStatus: UserStatus = UserStatus.ONLINE

    //    private var mDBReference: DatabaseReference? = null
//    private var mThreadReference: DatabaseReference? = null
    private var isChatDeleted = false

    //    private var groupValueEventListener: ValueEventListener? = null
//    private var groupMembersInfoAdapter: GroupMembersInfoAdapter? = null
    private var memberList: ArrayList<Member> = arrayListOf()
    private var isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            val arguments = arguments
            if (arguments != null) {
                groupId = arguments.getString("groupId")
//                isBlockedUser = arguments.getBoolean("isBlockedUser")
            }
        }
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener(this)
        binding.llDestruct.setOnClickListener(this)
        binding.llMute.setOnClickListener(this)
        binding.llBlock.setOnClickListener(this)
        binding.tvLeaveGroup.setOnClickListener(this)
        binding.ivAddMember.setOnClickListener(this)
        binding.tvDeleteThisChat.setOnClickListener(this)
        binding.tvEdit.setOnClickListener(this)
    }

    private fun initViews() {
//        mDBReference = AppClass.mDBReference
        initListeners()
        checkInternetAndInitialize()
//        checkChatUserBlockStatus(isBlockedUser)

        binding.rvGroupMembers.layoutManager = LinearLayoutManager(requireContext())
//        groupMembersInfoAdapter = GroupMembersInfoAdapter(memberList, groupId.toString(), this)
//        binding.rvGroupMembers.adapter = groupMembersInfoAdapter

        binding.llShowMembers.setOnClickListener {
//            groupMembersInfoAdapter?.seeAll = groupMembersInfoAdapter?.seeAll != true
            binding.llShowMembers.gone()
//            groupMembersInfoAdapter?.notifyDataSetChanged()
        }

        /* groupValueEventListener?.let {
             mDBReference
                 ?.child(Constants.TABLE_CHAT)
                 ?.child(Constants.TABLE_GROUPS)
                 ?.child(groupId.toString())
                 ?.addListenerForSingleValueEvent(it)
         }*/

        /*conversation = mDBReference
            ?.child(Constants.TABLE_CHAT)
            ?.child(Constants.TABLE_CONVERSATIONS)
            ?.child(session?.user?.userId.toString())//Receiver
            ?.child(groupId.toString())
*/
        /* conversation?.addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onDataChange(dataSnapshot: DataSnapshot) {
                 if (dataSnapshot.exists()) {
                     val values = dataSnapshot.value as HashMap<*, *>?
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

    private fun checkInternetAndInitialize() {
//        mThreadReference = mDBReference
//            ?.child(Constants.TABLE_CHAT)
//            ?.child(Constants.TABLE_THREADS)
//            ?.child(groupId.toString())
    }

    private fun initListeners() {
        /*        groupValueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val values = dataSnapshot.value as HashMap<*, *>?
                        if (values != null) {
                            binding.tvGroupName.text = values["name"].toString()
                            memberList.clear()
                            groupMembersInfoAdapter?.notifyDataSetChanged()
                            dataSnapshot.child("members").children.forEach {
                                val member = it.getValue(Member::class.java)
                                member?.let { it1 -> memberList.add(it1) }
                                groupMembersInfoAdapter?.notifyItemInserted(memberList.size - 1)
                                if (member?.isAdmin == true) {
                                    isAdmin = if (member.id == session?.user?.userId) {
                                        binding.tvEdit.visible()
                                        binding.tvLeaveGroup.gone()
                                        binding.tvDeleteThisChat.visible()
                                        true
                                    } else {
                                        binding.tvEdit.gone()
                                        binding.tvLeaveGroup.visible()
                                        binding.tvDeleteThisChat.gone()
                                        false
                                    }
                                }
                            }
                            if (memberList.isNotEmpty()) {
                                binding.llShowMembers.isVisible = memberList.size > 5
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
                }*/
    }

    private fun observeUser(model: ArrayList<Member>) {
        binding.llProfile2.isVisible = model.size > 1
        for (i in model.indices) {
            /* mDBReference?.child(Constants.TABLE_USERS)?.child(model[i].id.toString())
                 ?.addValueEventListener(object : ValueEventListener {
                     override fun onDataChange(dataSnapshot: DataSnapshot) {
                         val values = dataSnapshot.value as HashMap<*, *>?
                         Log.e("DataSnapshot", dataSnapshot.value.toString())

                         if (values != null) {
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
                                         .isBlank() || values["inChatWith"].toString() == groupId
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
                                         .isBlank() || values["inChatWith"].toString() == groupId
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
                 })*/
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> {
                preventDoubleClick(view)
                findNavController().popBackStack()
            }

            R.id.tvEdit -> {
                val bundle = Bundle()
                bundle.putString("groupId", groupId)
                findNavController().navigate(R.id.to_edit_group, bundle)
            }

            R.id.ivAddMember -> {
                val bundle = Bundle()
                bundle.putString("groupId", groupId)
                findNavController().navigate(R.id.to_add_members_to_group, bundle)
            }

            R.id.llMute -> {
                checkChatMuteStatus()
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
                            val params: MutableMap<String, Any> = HashMap()
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
                            val params: MutableMap<String, Any> = HashMap()
                            params["selfDestruct"] = SELF_DESTRUCT_OFF
//                            conversation?.updateChildren(params)
                        }
                    }
                }).show()
            }

            R.id.llBlock -> {
                preventDoubleClick(view)
            }

            R.id.tvDeleteThisChat -> {
                isChatDeleted = true
                preventDoubleClick(view)
                showDeleteDialog()
            }

            R.id.tvLeaveGroup -> {
                val deleteDialog = Dialog(requireContext(), R.style.DefaultDeleteThemeDialog)
                deleteDialog.setContentView(R.layout.dialog_delete_account)
                deleteDialog.show()
                val title = deleteDialog.findViewById<TextView>(R.id.tvTitle)
                val msg = deleteDialog.findViewById<TextView>(R.id.tvMessage)
                val btnCancel = deleteDialog.findViewById<TextView>(R.id.btnCancel)
                val btnOk = deleteDialog.findViewById<TextView>(R.id.btnOk)
                val ivIcon = deleteDialog.findViewById<ImageView>(R.id.ivIcon)
                btnCancel.text = getString(R.string.cancel)
                btnOk.text = getString(R.string.leave)
                title.gone()
                ivIcon.setImageResource(R.drawable.ic_warning)
                msg.text = getString(R.string.leave_group_text)
                btnOk.setOnClickListener {
                    deleteDialog.dismiss()
                    exitFromGroup()
                }
                btnCancel.setOnClickListener {
                    deleteDialog.dismiss()
                }
            }
        }
    }

    private fun exitFromGroup() {
//        val messageId = mThreadReference?.push()?.key
        val msg = "${session?.user?.userId}"

        /*  val message = Message(
              msg,
              messageId.toString(),
              session?.user?.userId.toString(),
              MessageType.LEFT_GROUP
          )
  */
        /*val mDBConversations = mDBReference
            ?.child(Constants.TABLE_CHAT)
            ?.child(Constants.TABLE_CONVERSATIONS)
*/
        /*mDBReference
            ?.child(Constants.TABLE_CHAT)
            ?.child(Constants.TABLE_CONVERSATIONS)
            ?.child(session?.user?.userId.toString())
            ?.child(groupId.toString())
            ?.removeValue()
            ?.addOnCompleteListener {
                mDBReference
                    ?.child(Constants.TABLE_CHAT)
                    ?.child(Constants.TABLE_GROUPS)
                    ?.child(groupId.toString())
                    ?.child("members")
                    ?.child(session?.user?.userId.toString())
                    ?.removeValue()
                    ?.addOnCompleteListener {

                        mDBReference
                            ?.child(Constants.TABLE_CHAT)
                            ?.child(Constants.TABLE_GROUPS)
                            ?.child(groupId.toString())//thread
                            ?.child("isTyping")
                            ?.child(session?.user?.userId.toString())
                            ?.removeValue()

                        mDBReference
                            ?.child(Constants.TABLE_CHAT)
                            ?.child(Constants.TABLE_GROUPS)
                            ?.child(groupId.toString())
                            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val values = dataSnapshot.value as HashMap<*, *>?

                                    if (values != null) {
                                        val members = dataSnapshot.child("members")
                                        //Injecting last message to conversation
                                        for (i in members.children) {
                                            //Adding in receiver's conversation
                                            val conversation = Conversation(
                                                groupId,
                                                true,
                                                values["name"].toString(),
                                                values["image"].toString(),
                                                0,
                                                message
                                            )
                                            mDBConversations?.child(i.key.toString())
                                                ?.child(groupId.toString())?.setValue(conversation)
                                        }
                                    }
                                    mThreadReference?.child(messageId.toString())?.setValue(message)
                                    findNavController().popBackStack(R.id.messages, false)
//                                    findNavController().navigateUp()
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    showToastLong(databaseError.message)
                                }
                            })

                    }
            }*/
    }

    private fun removeMemberFromGroup(memberId: String) {
//        val messageId = mThreadReference?.push()?.key

        /* val message = Message(
             memberId,
             messageId.toString(),
             session?.user?.userId,
             MessageType.DELETED_MEMBERS
         )*/

//        val mDBConversations = mDBReference
//            ?.child(Constants.TABLE_CHAT)
//            ?.child(Constants.TABLE_CONVERSATIONS)

        /*        mDBReference
                    ?.child(Constants.TABLE_CHAT)
                    ?.child(Constants.TABLE_CONVERSATIONS)
                    ?.child(memberId)
                    ?.child(groupId.toString())
                    ?.removeValue()
                    ?.addOnCompleteListener {
                        mDBReference
                            ?.child(Constants.TABLE_CHAT)
                            ?.child(Constants.TABLE_GROUPS)
                            ?.child(groupId.toString())
                            ?.child("members")
                            ?.child(memberId)
                            ?.removeValue()
                            ?.addOnCompleteListener {

                                mDBReference
                                    ?.child(Constants.TABLE_CHAT)
                                    ?.child(Constants.TABLE_GROUPS)
                                    ?.child(groupId.toString())//thread
                                    ?.child("isTyping")
                                    ?.child(session?.user?.userId.toString())
                                    ?.removeValue()

                                mDBReference
                                    ?.child(Constants.TABLE_CHAT)
                                    ?.child(Constants.TABLE_GROUPS)
                                    ?.child(groupId.toString())
                                    ?.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val values = dataSnapshot.value as HashMap<*, *>?

                                            if (values != null) {
                                                val members = dataSnapshot.child("members")
                                                //Injecting last message to conversation
                                                for (i in members.children) {
                                                    //Adding in receiver's conversation
                                                    val conversation = Conversation(
                                                        groupId,
                                                        true,
                                                        values["name"].toString(),
                                                        values["image"].toString(),
                                                        0,
                                                        message
                                                    )
                                                    mDBConversations?.child(i.key.toString())
                                                        ?.child(groupId.toString())?.setValue(conversation)
                                                }
                                            }
                                            mThreadReference?.child(messageId.toString())?.setValue(message)
                                            viewModel.isLoading.value = false
                                            findNavController().navigateUp()
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            showToastLong(databaseError.message)
                                        }
                                    })
                            }
                    }*/
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
//            mDBReference?.child(Constants.TABLE_CHAT)
//                ?.child(Constants.TABLE_GROUPS)
//                ?.child(groupId.toString())
//                ?.updateChildren(params)
//            findNavController().previousBackStackEntry!!
//                .savedStateHandle.set<Boolean>("chatDeleted", isChatDeleted)
            findNavController().navigateUp()
        }
        btnCancel.setOnClickListener {
            deleteDialog.dismiss()
        }
    }

    private fun checkChatMuteStatus() {
        val params: MutableMap<String, Any> = HashMap()
        if (binding.tvMute.text == getString(R.string.mute)) {
            binding.tvMute.text = (context as ActivityBase).getString(R.string.unmute)
            binding.ivMute.setImageResource(R.drawable.ic_mute_notifications)
            params["isChatMuted"] = true
        } else {
            binding.tvMute.text = (context as ActivityBase).getString(R.string.mute)
            binding.ivMute.setImageResource(R.drawable.ic_bell_bordered)
            params["isChatMuted"] = false
        }
        /* conversation?.updateChildren(params)
         mDBReference
             ?.child(Constants.TABLE_CHAT)
             ?.child(Constants.TABLE_GROUPS)
             ?.child(groupId.toString())?.updateChildren(params)*/
    }

    override fun onStop() {
        super.onStop()
        /*groupValueEventListener?.let {
            mDBReference
                ?.child(Constants.TABLE_CHAT)
                ?.child(Constants.TABLE_GROUPS)
                ?.child(groupId.toString())
                ?.removeEventListener(it)
        }*/
    }

    override fun onViewProfileSelected(dialog: GroupMemberInfoDialog, memberId: String) {
        dialog.dismiss()
        if (session?.user?.userId == memberId) {
            findNavController().navigate(R.id.profile)
        } else {
            val bundle = Bundle()
            bundle.putString("userId", memberId)
            findNavController().navigate(R.id.to_user_profile, bundle)
        }
    }

    override fun onRemoveFromGroupSelected(dialog: GroupMemberInfoDialog, memberId: String) {
        dialog.dismiss()
        viewModel.isLoading.value = true
        removeMemberFromGroup(memberId)
    }
}