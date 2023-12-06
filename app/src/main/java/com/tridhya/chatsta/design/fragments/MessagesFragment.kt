package com.tridhya.chatsta.design.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentMessagesBinding
import com.tridhya.chatsta.enum.chat.UserStatus
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.utils.GlideUtils

class MessagesFragment : BaseFragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentMessagesBinding

    //    private val adapter by lazy { ChatHistoryAdapter() }
//    private val groupAdapter by lazy { GroupChatHistoryAdapter() }
//    private var mDBReference: DatabaseReference? = null
    private var lastSeen: Long = 0
    private var isOnline: UserStatus = UserStatus.OFFLINE
    private var userSetStatus: UserStatus = UserStatus.ONLINE
//    private var userEventListener: ValueEventListener? = null
//    private var userStatusEventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentMessagesBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    private fun getChat() {
        /*mDBReference = AppClass.mDBReference
        mDBReference!!.child(Constants.TABLE_CHAT).child(Constants.TABLE_CONVERSATIONS)
            .child(session?.user?.userId.toString()).orderByChild("lastUpdate")
            .addValueEventListener(userEventListener!!)

        mDBReference
            ?.child(Constants.TABLE_USERS)?.child(session?.user?.userId.toString())
            ?.addValueEventListener(userStatusEventListener!!)*/

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setObservers()
        binding.ivClear.setOnClickListener(this)
        binding.ivCreate.setOnClickListener(this)
        binding.ivProfileImage.setOnClickListener(this)

        binding.etSearch.addTextChangedListener {
            if (it.toString().isBlank()) {
                binding.ivClear.gone()
            } else {
                binding.ivClear.visible()
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                adapter.filter.filter(p0)
//                groupAdapter.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setObservers() {
    }

    private fun initViews() {
        initListeners()
        if (!session?.user?.images.isNullOrEmpty())
            GlideUtils(requireActivity()).circleImage().loadImage(
                session?.user?.images?.get(0)?.image,
                binding.ivProfileImage
            )
        else
            GlideUtils(requireActivity()).circleImage().loadImage(
                R.drawable.ic_app,
                binding.ivProfileImage
            )

        binding.rvMessages.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        if (binding.tlSearch.selectedTabPosition == 0)
//            binding.rvMessages.adapter = adapter
        else
//            binding.rvMessages.adapter = groupAdapter
        /* adapter.setListener(
             object : OnItemClick {
                 override fun onItemClicked(obj: Any?) {
                     val data = obj as Conversation
                     val bundle = Bundle()
                     bundle.putSerializable("userId", data.userId)
                     findNavController().navigate(R.id.to_chat, bundle)
                 }
             })*/
        /* groupAdapter.setListener(
             object : OnItemClick {
                 override fun onItemClicked(obj: Any?) {
                     val data = obj as Conversation
                     val bundle = Bundle()
                     bundle.putSerializable("groupId", data.id)
                     findNavController().navigate(R.id.to_group_chat, bundle)
                 }
             })*/

            binding.tlSearch.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        when (tab?.position) {
                            1 -> {
//                            binding.rvMessages.adapter = groupAdapter
                            }

                            else -> {
//                            binding.rvMessages.adapter = adapter
                            }
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })

    }

    private fun initListeners() {
        /* userEventListener = object : ValueEventListener {
             override fun onDataChange(ds: DataSnapshot) {
                 if (ds.exists()) {
                     val list = arrayListOf<Conversation>()
                     val groupList = arrayListOf<Conversation>()
                     var unreadCount = 0
                     var unreadGroupCount = 0
                     ds.children.forEach { it ->
                         it.getValue(Conversation::class.java)?.let {
                             if (it.unreadCount != null && it.unreadCount!! > 0) {
                                 if (it.group) ++unreadGroupCount else ++unreadCount
                             }
                             if (it.group) groupList.add(it) else list.add(it)
                         }
                     }
                     list.reverse()
                     groupList.reverse()
                     adapter.setList(list)
                     groupAdapter.setList(groupList)
                     if (unreadCount != 0) {
                         val badge = binding.tlSearch.getTabAt(0)?.orCreateBadge
                         badge?.number = unreadCount
                         if (isAdded) {
                             badge?.badgeTextColor =
                                 ContextCompat.getColor(
                                     requireActivity().applicationContext,
                                     R.color.white
                                 )
                             badge?.backgroundColor =
                                 ContextCompat.getColor(
                                     requireActivity().applicationContext,
                                     R.color.purple
                                 )
                         }
                         badge?.horizontalOffset = -20
                     } else {
                         binding.tlSearch.getTabAt(0)?.removeBadge()
                     }
                     if (unreadGroupCount != 0) {
                         val badge = binding.tlSearch.getTabAt(1)?.orCreateBadge
                         badge?.number = unreadGroupCount
                         if (isAdded) {
                             badge?.badgeTextColor =
                                 ContextCompat.getColor(
                                     requireActivity().applicationContext,
                                     R.color.white
                                 )
                             badge?.backgroundColor =
                                 ContextCompat.getColor(
                                     requireActivity().applicationContext,
                                     R.color.purple
                                 )
                         }
                         badge?.horizontalOffset = -20
                     } else {
                         binding.tlSearch.getTabAt(1)?.removeBadge()
                     }
                 } else {
                     adapter.setList(arrayListOf())
                     groupAdapter.setList(arrayListOf())
                     binding.tlSearch.getTabAt(0)?.removeBadge()
                     binding.tlSearch.getTabAt(1)?.removeBadge()
                 }

                 groupAdapter.notifyDataSetChanged()
                 adapter.notifyDataSetChanged()
             }

             override fun onCancelled(de: DatabaseError) {
                 showToastLong(de.message)
             }
         }

         userStatusEventListener = object : ValueEventListener {
             override fun onDataChange(dataSnapshot: DataSnapshot) {
                 val values = dataSnapshot.value as HashMap<*, *>?
                 Log.e("DataSnapshot---MessagesFragment", dataSnapshot.value.toString())

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

                     if (userSetStatus == UserStatus.ONLINE) {
                         val status = if (inChatWith.isNotBlank()) {
                             if (getMessageIdChatWithMe(session?.user?.userId.toString()) == inChatWith) {
                                 isOnline
                             } else {
                                 UserStatus.BUSY
                             }
                         } else {
                             isOnline
                         }
                         when (status) {
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
                 }
             }

             override fun onCancelled(databaseError: DatabaseError) {}
         }*/
    }

    override fun onStop() {
        super.onStop()
        /*if (userEventListener != null) {
            mDBReference!!.child(Constants.TABLE_CHAT).child(Constants.TABLE_CONVERSATIONS)
                .child(session?.user?.userId.toString()).orderByChild("lastUpdate")
                .removeEventListener(userEventListener!!)
        }

        if (userStatusEventListener != null) {
            mDBReference
                ?.child(Constants.TABLE_USERS)?.child(session?.user?.userId.toString())
                ?.removeEventListener(userStatusEventListener!!)
        }*/
    }

    override fun onResume() {
        super.onResume()
        getChat()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            Constants.TAG
        )?.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(
                    Constants.TAG
                )
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        findNavController().navigate(R.id.settingsDialog)
                    },
                    10
                )
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClear -> {
                preventDoubleClick(view)
                binding.etSearch.setText("")
            }

            R.id.ivCreate -> {
                preventDoubleClick(view)
                findNavController().navigate(R.id.to_create_chat)
            }

            R.id.ivProfileImage -> {
                preventDoubleClick(view)
                binding.ivProfileImage.isEnabled = false
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.ivProfileImage.isEnabled = true
                }, 1000)
                findNavController().navigate(R.id.to_user_status)
            }
        }
    }
}