package com.tridhya.chatsta.design.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentMyConnectionsBinding
import com.tridhya.chatsta.design.dialogs.feed.ConnectionsMoreOptionsBottomDialog
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.provider.Constants


class MyConnectionsFragment : BaseFragment(), View.OnClickListener,
    ConnectionsMoreOptionsBottomDialog.OptionSelectedListener {

    //    private val viewModel by lazy { FeedViewModel(requireContext()) }
    private lateinit var binding: FragmentMyConnectionsBinding
    private var page: Int = 1
    var position = 0
    var isBlock = false
    private var threadId: String? = null
//    private var mDBReference: DatabaseReference? = null
    /* private val connectionsAdapter by lazy {
         ConnectionsAdapter(
             listener = this,
             loadMoreListener = this,
             isCard = false
         )
     }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentMyConnectionsBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            page = 1
//            viewModel.isLoading.value = true
            getSearchedList("")
//            viewModel.myConnectionRequests()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mDBReference = AppClass.mDBReference
        setObservers()
        binding.ivClose.setOnClickListener(this)
        binding.llConnectionRequests.setOnClickListener(this)
        binding.ivClear.setOnClickListener(this)
        binding.rvConnections.layoutManager = LinearLayoutManager(requireContext())

        binding.etSearch.addTextChangedListener {
            if (it.toString().isBlank()) {
                binding.ivClear.gone()
            } else {
                binding.ivClear.visible()
            }
        }

        binding.etSearch.setOnEditorActionListener { textView, actionId, event ->
//            connectionsAdapter.clearList()
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
//                viewModel.isRefreshing.value = true
                page = 1
                getSearchedList(binding.etSearch.text.toString())
            }
            false
        }
    }

    private fun setObservers() {
        /*viewModel.responseMyConnections.observe(viewLifecycleOwner) {
            if (it.connections.isNullOrEmpty() && page == 1) {
                connectionsAdapter.clearList()
                binding.rvConnections.gone()
                binding.tvNoConnections.visible()
                binding.tvConnectionCount.text = getString(R.string._0)
            } else {
                binding.tvConnectionCount.text = it.connectionsCount.toString()
                binding.rvConnections.visible()
                binding.tvNoConnections.gone()
                binding.rvConnections.adapter = connectionsAdapter
                if (page == 1) {
                    connectionsAdapter.setList(it.connections)
                } else {
                    connectionsAdapter.addList(it.connections)
                }
            }
            if (viewModel.responseMyConnectionRequests.value != null)
                viewModel.isLoading.value = false
        }

        viewModel.responseMyConnectionRequests.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            if (it.connectionRequestsCount != null && it.connectionRequestsCount!! > 0) {
                binding.tvRequestCount.visible()
                binding.tvRequestCount.text = it.connectionRequestsCount.toString()
            } else
                binding.tvRequestCount.gone()
            if (it.connections.isNullOrEmpty())
                binding.llConnectionRequests.gone()
            else {
                binding.llConnectionRequests.visible()
                if (it.connections!!.size < 2) {
                    binding.ivProfile2.gone()
                    if (!it.connections!![0].images.isNullOrEmpty())
                        GlideUtils(requireContext()).circleImage()
                            .loadImage(
                                it.connections!![0].images?.get(0)?.image,
                                binding.ivProfile1
                            )
                    else
                        GlideUtils(requireContext()).circleImage()
                            .loadImage(R.drawable.ic_app, binding.ivProfile1)
                } else if (it.connections!!.size >= 2) {
                    binding.ivProfile2.visible()
                    if (!it.connections!![0].images.isNullOrEmpty())
                        GlideUtils(requireContext()).circleImage()
                            .loadImage(
                                it.connections!![0].images?.get(0)?.image,
                                binding.ivProfile1
                            )
                    else
                        GlideUtils(requireContext()).circleImage()
                            .loadImage(R.drawable.ic_app, binding.ivProfile1)

                    if (!it.connections!![1].images.isNullOrEmpty())
                        GlideUtils(requireContext()).circleImage()
                            .loadImage(
                                it.connections!![1].images?.get(0)?.image,
                                binding.ivProfile2
                            )
                    else
                        GlideUtils(requireContext()).circleImage()
                            .loadImage(R.drawable.ic_app, binding.ivProfile2)
                }
            }
            if (viewModel.responseMyConnections.value != null)
                viewModel.isLoading.value = false
        }

        viewModel.responseDisconnectConnection.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            connectionsAdapter.updateConnectionStatus(position, Constants.NORMAL_USER)
            viewModel.isRefreshing.value = true
            page = 1
            getSearchedList(binding.etSearch.text.toString())
        }

        viewModel.responseBlockUnblockConnection.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            connectionsAdapter.updateBlockUnblockStatus(
                position,
                if (isBlock) Constants.BLOCK_USER else Constants.UNBLOCK_USER
            )

            val params: MutableMap<String, Any> = HashMap()
            params["blockedBy_${session?.user?.userId}"] = isBlock

            mDBReference
                ?.child(Constants.TABLE_CHAT)
                ?.child(Constants.TABLE_THREADS)
                ?.child(threadId.toString())
                ?.updateChildren(params)
        }

        viewModel.responseCancelConnectionRequest.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            connectionsAdapter.updateConnectionStatus(position, Constants.NORMAL_USER)
        }

        viewModel.responseDeclineConnectionRequest.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            connectionsAdapter.updateConnectionStatus(
                position,
                Constants.NORMAL_USER
            )
        }

        viewModel.responseAcceptConnectionRequest.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            connectionsAdapter.updateConnectionStatus(
                position,
                Constants.CONNECTED
            )
        }*/
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose -> {
                findNavController().navigateUp()
            }

            R.id.llConnectionRequests -> {
                findNavController().navigate(R.id.to_connection_requests)
            }

            R.id.ivClear -> {
                preventDoubleClick(view)
                binding.etSearch.setText("")
//                connectionsAdapter.clearList()
//                viewModel.isRefreshing.value = true
                page = 1
                getSearchedList(binding.etSearch.text.toString())
//                viewModel.myConnectionRequests()
            }
        }
    }

    /* override fun onMoreOptions(userData: User, isBlock: Boolean, position: Int) {
         this.position = position
         activity?.supportFragmentManager?.let {
             ConnectionsMoreOptionsBottomDialog.newInstance(
                 userData,
                 isBlock,
                 this
             ).show(
                 it, Constants.TAG
             )
         }
     }*/

//    override fun onRequestSend(userData: User, position: Int) {
//        this.position = position
//        viewModel.isLoading.value = true
//        userData.userId?.let { viewModel.requestConnection(userId = it) }
//    }

    /*override fun onChatSelected(userData: User, view: View) {
        preventDoubleClick(view)
        val bundle = Bundle()
        bundle.putString("userId", userData.userId)
        findNavController().navigate(R.id.to_chat, bundle)
    }

    override fun onAcceptRequestClicked(userData: User, position: Int) {
        this.position = position
        viewModel.isLoading.value = true
        userData.userId?.let { viewModel.acceptRequestConnection(userId = it) }
    }

    override fun onProfileClicked(userData: User) {
        val bundle = Bundle()
        bundle.putString("userId", userData.userId)
        findNavController().navigate(R.id.to_user_profile, bundle)
    }

    override fun onLoadMore() {
        ++page
        getSearchedList(binding.etSearch.text.toString())
    }
*/
    private fun getSearchedList(searchText: String) {
//        viewModel.isLoading.value = true
//        viewModel.myConnections(
//            searchText.trim(),
//            page
//        )
//        connectionsAdapter.clearList()
        binding.rvConnections.gone()
        binding.tvNoConnections.visible()
        binding.tvConnectionCount.text = getString(R.string._0)
    }

    override fun onResume() {
        super.onResume()
        page = 1
//        getSearchedList(binding.etSearch.text.toString())
//        viewModel.myConnectionRequests()
    }

    override fun onBlockSelected(dialog: ConnectionsMoreOptionsBottomDialog, userData: User) {
//        viewModel.isLoading.value = true
        isBlock = true
        threadId = getMessageIdChatWithMe(userData.userId.toString())
//        userData.userId?.let { viewModel.blockUnblockConnection(userId = it) }
    }

    override fun onUnblockSelected(dialog: ConnectionsMoreOptionsBottomDialog, userData: User) {
//        viewModel.isLoading.value = true
        isBlock = false
        threadId = getMessageIdChatWithMe(userData.userId.toString())
//        userData.userId?.let { viewModel.blockUnblockConnection(userId = it) }
    }

    override fun onDisconnectSelected(dialog: ConnectionsMoreOptionsBottomDialog, userData: User) {
        when (userData.userConnectionStatus) {
            Constants.ALREADY_REQUESTED -> {
//                viewModel.isLoading.value = true
//                userData.userId?.let { viewModel.cancelConnectionRequest(userId = it) }
            }

            Constants.REQUEST_RECEIVED -> {
//                viewModel.isLoading.value = true
//                userData.userId?.let { viewModel.deleteRequestConnection(userId = it) }
            }

            else -> {
//                viewModel.isLoading.value = true
//                userData.userId?.let { viewModel.disconnectConnection(userId = it) }
            }
        }
    }
}