package com.tridhya.chatsta.design.fragments.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentNotificationBinding
import com.tridhya.chatsta.design.fragments.BaseFragment

class NotificationFragment : BaseFragment(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentNotificationBinding

    //    private var acceptClicked: NotificationsAdapter.OnRequestAccept? = null
//    private var declineClicked: NotificationsAdapter.OnRequestDecline? = null
//    private val viewModel by lazy { NotificationViewModel(requireContext()) }
    private var page: Int = 1
    /*private val notificationAdapter by lazy {
        NotificationsAdapter(
            listener = this,
            loadMoreListener = this
        )
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentNotificationBinding.inflate(inflater, container, false)
            initViews()
            binding.lifecycleOwner = viewLifecycleOwner
            page = 1
//            viewModel.isLoading.value = true
//            viewModel.getAllNotifications(page)
        }
        return binding.root
    }

    private fun setObservers() {
        /* viewModel.isRefreshing.observe(viewLifecycleOwner) {
             binding.swipeRefresh.isRefreshing = it
         }*/

    }

    private fun initViews() {
        binding.rvNewNotifications.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvNewNotifications.adapter = notificationAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        binding.ivClose.setOnClickListener(this)
        binding.swipeRefresh.setOnRefreshListener(this)

        binding.tlSearch.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
//                notificationAdapter.clearList()
                binding.rvNewNotifications.adapter = null
//                viewModel.isRefreshing.value = false
//                viewModel.isLoading.value = true
                page = 1
                getNotificationList()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }

    /*    override fun onClicked(data: NotificationsBaseModel, isRead: Boolean, position: Int) {
            val type: String = data.type.toString()
            val bundle = Bundle()
            val postId = data.postId
            val commentId = data.commentId
            val userId = data.user?.userId
            val reactionId = data.reactionId
            val chatId = data.chatId
            val notificationId = data.id

            if (postId != null) bundle.putString(Constants.POST_ID, postId)
            if (commentId != null) bundle.putString(Constants.COMMENT_ID, commentId)
            if (userId != null) bundle.putString(Constants.USER_ID, userId)
            if (reactionId != null) bundle.putString(Constants.REACTION_ID, reactionId)
            if (chatId != null) bundle.putString(Constants.CHAT_ID, chatId)
            if (notificationId != null) bundle.putString(Constants.NOTIFICATION_ID, notificationId)

            when (type) {
                "chat_message" -> {
                }

                Constants.COMMENT_TYPE -> {
                    findNavController().navigate(R.id.userComments, bundle)
                }

                Constants.REACTION_TYPE -> {
                    findNavController().navigate(R.id.profile)
                }

                Constants.REQUEST_TYPE -> {
                    findNavController().navigate(R.id.connectionRequests, bundle)
                }

                Constants.CHAT_TYPE -> {
                    findNavController().navigate(R.id.chat, bundle)
                }

                Constants.GROUP_TYPE -> {
                    val bundle2 = Bundle()
                    bundle2.putString(Constants.GROUP_ID, bundle.getString(Constants.CHAT_ID))
                    findNavController().navigate(R.id.groupChat, bundle2)
                }
            }
        }*/

    private fun getNotificationList() {
        if (binding.tlSearch.selectedTabPosition == 0) {
//            viewModel.getAllNotifications(page)
        } else {
//            viewModel.getAllNotifications(page, true)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose -> {
                findNavController().navigateUp()
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRefresh() {
        page = 1
        getNotificationList()
    }
}