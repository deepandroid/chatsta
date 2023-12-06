package com.chatsta.ui.home.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentConnectionRequestBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible

class ConnectionRequestFragment : BaseFragment(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    //    private val viewModel by lazy { FeedViewModel(requireContext()) }
    private lateinit var binding: FragmentConnectionRequestBinding
    private var page: Int = 1

    //    private var acceptClicked: ConnectionRequestAdapter.OnRequestAccept? = null
//    private var declineClicked: ConnectionRequestAdapter.OnRequestDecline? = null
    private val connectionRequestAdapter by lazy {
        /* ConnectionRequestAdapter(
             listener = this,
             loadMoreListener = this
         )*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentConnectionRequestBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            page = 1
//            viewModel.isLoading.value = true
//            viewModel.myConnectionRequests()
            initViews()
        }
        return binding.root
    }

    private fun initViews() {
        binding.rvConnectionRequests.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        binding.ivClose.setOnClickListener(this)
        binding.ivDelete.setOnClickListener(this)
        binding.swipeRefresh.setOnRefreshListener(this)
    }

    private fun setObserver() {
        /*viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }
*/
        /*viewModel.responseMyConnectionRequests.observe(viewLifecycleOwner) {
            viewModel.isRefreshing.value = false
            viewModel.isLoading.value = false
            if (it.connections.isNullOrEmpty() && page == 1) {
                binding.tvNoRequests.visible()
                binding.rvConnectionRequests.gone()
                binding.tvRequestCount.gone()
                binding.ivDelete.gone()
            } else {
                binding.tvRequestCount.visible()
                binding.ivDelete.visible()
                binding.tvRequestCount.text = it.connectionRequestsCount.toString()
                binding.rvConnectionRequests.visible()
                binding.tvNoRequests.gone()
                binding.rvConnectionRequests.adapter = connectionRequestAdapter
                if (page == 1) {
                    connectionRequestAdapter.setList(it.connections)
                } else {
                    connectionRequestAdapter.addList(it.connections)
                }
            }
        }

        viewModel.responseAcceptConnectionRequest.observe(viewLifecycleOwner) {
            viewModel.isRefreshing.value = false
            viewModel.isLoading.value = false
            if (!(it.isNullOrBlank() || it.isNullOrEmpty())) {
                acceptClicked?.onAccept()
                connectionRequestAdapter.notifyDataSetChanged()
            }
        }

        viewModel.responseDeclineConnectionRequest.observe(viewLifecycleOwner) {
            viewModel.isRefreshing.value = false
            viewModel.isLoading.value = false
            if (!(it.isNullOrBlank() || it.isNullOrEmpty())) {
                declineClicked?.onDecline()
                connectionRequestAdapter.notifyDataSetChanged()
            }
        }

        viewModel.responseDeleteAllConnectionRequest.observe(viewLifecycleOwner) {
            viewModel.isRefreshing.value = false
            viewModel.isLoading.value = false
            if (!(it.isNullOrBlank() || it.isNullOrEmpty())) {
                connectionRequestAdapter.clearList()
                page = 1
                viewModel.isLoading.value = true
                viewModel.myConnectionRequests()
            }
        }*/
    }

    /* override fun onConnectionClicked(userData: User) {
         val bundle = Bundle()
         bundle.putString("userId", userData.userId)
         findNavController().navigate(R.id.to_user_profile, bundle)
     }*/

//    override fun onAcceptClicked(
//        userData: User,
//        onRequestAccept: ConnectionRequestAdapter.OnRequestAccept,
//    ) {
//        viewModel.isLoading.value = true
//        acceptClicked = onRequestAccept
//        userData.userId?.let { viewModel.acceptRequestConnection(userId = it) }
//    }

    /* override fun onDeclineClicked(
         userData: User,
         onRequestDecline: ConnectionRequestAdapter.OnRequestDecline,
     ) {
         viewModel.isLoading.value = true
         declineClicked = onRequestDecline
         userData.userId?.let { viewModel.deleteRequestConnection(userId = it) }
     }
 */
    /* override fun onLoadMore() {
         ++page
         viewModel.isRefreshing.value = true
         viewModel.myConnectionRequests()
     }
 */
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose -> {
                findNavController().navigateUp()
            }

            R.id.ivDelete -> {
                MessageDialog.getInstance(
                    requireContext(),
                    getString(R.string.delete_all_request_msg)
                )
                    .setIcon(R.drawable.ic_delete_rounded)
                    .setPositiveButtonText(R.string.delete)
                    .setNegativeButton(R.string.cancel)
                    .setListener(object : MessageDialog.ButtonListener {
                        override fun onPositiveBtnClicked(dialog: MessageDialog) {
                            dialog.dismiss()
//                            viewModel.isLoading.value = true
//                            viewModel.deleteAllRequestConnection(userId = DELETE_ALL_REQUESTS)
                            binding.tvNoRequests.visible()
                            binding.rvConnectionRequests.gone()
                            binding.tvRequestCount.gone()
                            binding.ivDelete.gone()
                        }
                    })
                    .show()
            }
        }
    }

    override fun onRefresh() {
        page = 1
//        viewModel.isRefreshing.value = true
//        viewModel.myConnectionRequests()
    }

    override fun onResume() {
        super.onResume()
//        viewModel.isLoading.value = true
//        viewModel.myConnectionRequests()
    }

}