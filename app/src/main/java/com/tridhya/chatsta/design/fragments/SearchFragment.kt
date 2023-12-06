package com.tridhya.chatsta.design.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentSearchBinding
import com.tridhya.chatsta.design.dialogs.feed.FeedFilterBottomDialog
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.invisible
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.provider.Constants.MOST_RECENT

class SearchFragment : BaseFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    FeedFilterBottomDialog.OptionListener {

    private lateinit var binding: FragmentSearchBinding
    var count = 0
    var position = 0
    var isBlock = false
    private var threadId: String? = null

    private var selectedValue: String? = MOST_RECENT
    private var gifUrl: String? = null
    private var page: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
//        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        if (!this::binding.isInitialized) {
            binding = FragmentSearchBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            getSearchedList(binding.toolbarSearch.etSearch.text.toString())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.getAllSponsorClock()

        initViews()
        setObservers()
        binding.toolbarSearch.ivClear.setOnClickListener(this)
        binding.toolbarSearch.tvCancel.setOnClickListener(this)
        binding.tvFilterType.setOnClickListener(this)

        binding.toolbarSearch.etSearch.addTextChangedListener {
            if (it.toString().isBlank()) {
                binding.toolbarSearch.ivClear.gone()
            } else {
                binding.toolbarSearch.ivClear.visible()
            }
        }

        binding.tlSearch.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
//                feedAdapter.stopPlayers()
                when (tab?.position) {
                    0 -> {
                        binding.llFilterType.invisible()
                        /* feedAdapter.clearList()
                         publicPostsAdapter.clearList()
                         connectionsAdapter.clearList()*/
                        binding.rvSearchFeed.adapter = null
//                        viewModel.isRefreshing.value = false
//                        binding.rvSearchFeed.adapter = feedAdapter
                        page = 1
                        getSearchedList(binding.toolbarSearch.etSearch.text.toString())
                    }

                    1 -> {
                        binding.llFilterType.invisible()
                        /*feedAdapter.clearList()
                        publicPostsAdapter.clearList()
                        connectionsAdapter.clearList()*/
                        binding.rvSearchFeed.adapter = null
//                        binding.rvSearchFeed.adapter = publicPostsAdapter
//                        viewModel.isRefreshing.value = false
                        page = 1
                        getSearchedList(binding.toolbarSearch.etSearch.text.toString())
                    }

                    2 -> {
                        binding.llFilterType.invisible()
                        binding.rvSearchFeed.adapter = null
                        /*feedAdapter.clearList()
                        publicPostsAdapter.clearList()
                        connectionsAdapter.clearList()*/
//                        binding.rvSearchFeed.adapter = connectionsAdapter
//                        viewModel.isRefreshing.value = false
                        page = 1
                        if (binding.toolbarSearch.etSearch.text.toString().isNotBlank())
                            getSearchedList(binding.toolbarSearch.etSearch.text.toString())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        binding.swipeRefresh.setOnRefreshListener(this)

        binding.toolbarSearch.etSearch.setOnEditorActionListener { textView, actionId, event ->
            /* feedAdapter.clearList()
             connectionsAdapter.clearList()
             publicPostsAdapter.clearList()*/
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
//                viewModel.isRefreshing.value = true
                page = 1
                getSearchedList(binding.toolbarSearch.etSearch.text.toString())
            }
            false
        }
    }

    private fun initViews() {
        selectedValue = MOST_RECENT
//        viewModel.isLoading.value = true
        binding.rvSearchFeed.layoutManager = LinearLayoutManager(requireContext())
//        getSearchedList(binding.toolbarSearch.etSearch.text.toString())
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClear -> {
                preventDoubleClick(view)
                binding.toolbarSearch.etSearch.setText("")
                /*viewModel.isRefreshing.value = true
                feedAdapter.clearList()
                publicPostsAdapter.clearList()
                connectionsAdapter.clearList()*/
                getSearchedList(binding.toolbarSearch.etSearch.text.toString())
            }

            R.id.tvFilterType -> {
                activity?.supportFragmentManager?.let {
                    FeedFilterBottomDialog.newInstance(selectedValue, this).show(
                        it, Constants.TAG
                    )
                }
            }

            R.id.tvCancel -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun setObservers() {
        /*  viewModel.isRefreshing.observe(viewLifecycleOwner) {
              binding.swipeRefresh.isRefreshing = it
          }
  */
        /*viewModel.responseSponsorClockItems.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            if (!it.isNullOrEmpty() && !(it[0].companyAd.isNullOrBlank() || it[0].companyAd.isNullOrEmpty()) &&
                !(it[0].compnyLogo.isNullOrBlank() || it[0].compnyLogo.isNullOrEmpty())
            ) {
                Glide.with(requireContext())
                    .load(it[0].companyAd)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            p0: GlideException?,
                            p1: Any?,
                            p2: Target<Drawable>?,
                            p3: Boolean,
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            p0: Drawable?,
                            p1: Any?,
                            p2: Target<Drawable>?,
                            p3: DataSource?,
                            p4: Boolean,
                        ): Boolean {
                            binding.ivSponsorAd.visible()
                            binding.ivSponsorLogo.visible()
                            binding.ivStaticSponsor.gone()
                            return false
                        }
                    })
                    .into(binding.ivSponsorAd)

                Glide.with(requireContext())
                    .load(it[0].compnyLogo)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            p0: GlideException?,
                            p1: Any?,
                            p2: Target<Drawable>?,
                            p3: Boolean,
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            p0: Drawable?,
                            p1: Any?,
                            p2: Target<Drawable>?,
                            p3: DataSource?,
                            p4: Boolean,
                        ): Boolean {
                            binding.ivSponsorAd.visible()
                            binding.ivSponsorLogo.visible()
                            binding.ivStaticSponsor.gone()
                            return false
                        }
                    })
                    .into(binding.ivSponsorLogo)
            } else {
                binding.ivSponsorLogo.gone()
                binding.ivSponsorAd.gone()
                binding.ivStaticSponsor.visible()
            }
        }*/

        /* viewModel.responseDeletePosts.observe(viewLifecycleOwner) {
             viewModel.isLoading.value = false
             if (it != null) {
                 showToastShort(it)
                 viewModel.isRefreshing.value = true
                 page = 1
                 getSearchedList(binding.toolbarSearch.etSearch.text.toString())
                 viewModel.responseDeletePosts.value = null
             }
         }
 */
        /* viewModel.responseReportPosts.observe(viewLifecycleOwner) {
             viewModel.isLoading.value = false
             if (it != null) {
                 showToastShort(it)
                 viewModel.responseReportPosts.value = null
             }
         }*/
        /*
                viewModel.responseGetPosts.observe(viewLifecycleOwner) {
                    it.data?.postList?.forEach { post ->
                        post.reactions.forEach { reaction ->
                            Glide.with(requireContext()).asGif().load(reaction.gifUrl)
                        }
                    }
                    if (binding.tlSearch.selectedTabPosition == 0 || binding.tlSearch.selectedTabPosition == 1) {
                        viewModel.isLoading.value = false
                        viewModel.isRefreshing.value = false
                        if (binding.toolbarSearch.etSearch.text.isNullOrBlank() && it.data?.postList.isNullOrEmpty() && page == 1) {
                            binding.rvSearchFeed.gone()
                            binding.llFilterType.invisible()
                            binding.tvNotFound.gone()
                        } else if (!binding.toolbarSearch.etSearch.text.isNullOrBlank() && it.data?.postList.isNullOrEmpty() && page == 1) {
                            binding.rvSearchFeed.gone()
                            binding.llFilterType.gone()
                            binding.tvNotFound.visible()
                            when (binding.tlSearch.selectedTabPosition) {
                                0 -> {
                                    binding.tvNotFound.text =
                                        "0 feed found for \"${binding.toolbarSearch.etSearch.text}\""
                                }

                                1 -> {
                                    binding.tvNotFound.text =
                                        "0 public posts found for \"${binding.toolbarSearch.etSearch.text}\""
                                }

                                2 -> {
                                    binding.tvNotFound.text =
                                        "0 connections found for \"${binding.toolbarSearch.etSearch.text}\""
                                }
                            }
                        } else {
                            binding.rvSearchFeed.visible()
                            binding.llFilterType.visible()
                            binding.tvNotFound.gone()
                            when (binding.tlSearch.selectedTabPosition) {
                                0 -> {
                                    if (page == 1) {
                                        binding.rvSearchFeed.adapter = feedAdapter
                                        feedAdapter.setList(it.data?.postList)
                                    } else {
                                        feedAdapter.addList(it.data?.postList)
                                    }
                                }

                                1 -> {
                                    if (page == 1) {
                                        binding.rvSearchFeed.adapter = publicPostsAdapter
                                        publicPostsAdapter.setList(it.data?.postList)
                                    } else {
                                        publicPostsAdapter.addList(it.data?.postList)
                                    }
                                }
                            }
                        }
                    }
                }

                viewModel.responseSearchConnections.observe(viewLifecycleOwner) {
                    if (binding.tlSearch.selectedTabPosition == 2) {
                        viewModel.isLoading.value = false
                        viewModel.isRefreshing.value = false
                        if (!binding.toolbarSearch.etSearch.text.isNullOrBlank() && it.isNullOrEmpty() && page == 1) {
                            binding.rvSearchFeed.gone()
                            binding.llFilterType.gone()
                            binding.tvNotFound.visible()
                            when (binding.tlSearch.selectedTabPosition) {
                                0 -> {
                                    binding.tvNotFound.text =
                                        "0 feed found for \"${binding.toolbarSearch.etSearch.text}\""
                                }

                                1 -> {
                                    binding.tvNotFound.text =
                                        "0 public posts found for \"${binding.toolbarSearch.etSearch.text}\""
                                }

                                2 -> {
                                    binding.tvNotFound.text =
                                        "0 connections found for \"${binding.toolbarSearch.etSearch.text}\""
                                }
                            }
                        } else {
                            binding.rvSearchFeed.visible()
                            binding.llFilterType.invisible()
                            binding.tvNotFound.gone()
                            when (binding.tlSearch.selectedTabPosition) {
                                2 -> {
                                    binding.rvSearchFeed.adapter = connectionsAdapter
                                    if (page == 1) {
                                        connectionsAdapter.setList(it)
                                    } else {
                                        connectionsAdapter.addList(it)
                                    }
                                }
                            }
                        }
                    }
                }

                viewModel.responseReactionsModel.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    if (it != null) {
                        val user = session?.user
                        user?.freeReaction = it.freereaction
                        session?.user = user
                        if (successListener != null) {
                            it.totalReactionsCount?.let { it1 ->
                                successListener!!.onSuccessReactionClick(
                                    it.totalDonation,
                                    it1
                                )
                            }
                        }
                        gifUrl?.let { it1 -> (requireContext() as MainActivity).showGIF(it1) }
                        viewModel.responseReactionsModel.value = null
                    }
                }

                viewModel.responsePostUserComments.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    viewModel.commentText.set("")
                    if (successPostCommentListener != null) {
                        it?.let {
                            it.totalcount?.let { it1 ->
                                successPostCommentListener!!.onSuccessPostComment(
                                    it1,
                                )
                            }
                        }
                    }
                }

                viewModel.responseDisconnectConnection.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    connectionsAdapter.updateConnectionStatus(position, NORMAL_USER)
                }

                viewModel.responseCancelConnectionRequest.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    connectionsAdapter.updateConnectionStatus(position, NORMAL_USER)
                }

                viewModel.responseSendConnectionRequest.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    connectionsAdapter.updateConnectionStatus(position, ALREADY_REQUESTED)
                }

                viewModel.responseBlockUnblockConnection.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    connectionsAdapter.updateBlockUnblockStatus(
                        position,
                        if (isBlock) BLOCK_USER else UNBLOCK_USER
                    )

                    val params: MutableMap<String, Any> = HashMap()
                    params["blockedBy_${session?.user?.userId}"] = isBlock

                    mDBReference
                        ?.child(Constants.TABLE_CHAT)
                        ?.child(Constants.TABLE_THREADS)
                        ?.child(threadId.toString())
                        ?.updateChildren(params)
                }

                viewModel.responseDeclineConnectionRequest.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    connectionsAdapter.updateConnectionStatus(
                        position,
                        NORMAL_USER
                    )
                }

                viewModel.responseAcceptConnectionRequest.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    connectionsAdapter.updateConnectionStatus(
                        position,
                        CONNECTED
                    )
                }

                viewModel.refreshLiveData.observe(viewLifecycleOwner) {
                    page = 1
                    getSearchedList(binding.toolbarSearch.etSearch.text.toString())
                }*/
    }

    override fun onResume() {
        super.onResume()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            Constants.TAG
        )
            ?.observe(
                viewLifecycleOwner
            ) {
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

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(
            "count"
        )
            ?.observe(
                this
            ) {
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Int>(
                    "count"
                )
                count = it
            }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(
            "postPosition"
        )
            ?.observe(
                this
            ) {
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Int>(
                    "postPosition"
                )
//                feedAdapter.changeCommentCount(it, count)
            }

        if (binding.tlSearch.selectedTabPosition == 2) {
            page = 1
            getSearchedList(binding.toolbarSearch.etSearch.text.toString())
        }

    }


    override fun onRefresh() {
        page = 1
        getSearchedList(binding.toolbarSearch.etSearch.text.toString())
    }


    fun getSearchedList(searchText: String) {
        val filterBy = selectedValue
//        viewModel.isRefreshing.value = true
        when (binding.tlSearch.selectedTabPosition) {
            0 -> {
                /* viewModel.getPosts(
                     filter = filterBy.toString(),
                     searchText.trim(),
                     Constants.TYPE_FEED,
                     true,
                     page
                 )*/
            }

            1 -> {
                /* viewModel.getPosts(
                     filter = filterBy.toString(),
                     searchText.trim(),
                     Constants.TYPE_PUBLIC,
                     searchText.isNotBlank(),
                     page
                 )*/
            }

            2 -> {
                /*viewModel.getSearchedConnections(
                    searchText.trim(),
                    page
                )*/
            }
        }
    }

    override fun onOptionSelected(dialog: FeedFilterBottomDialog, filterBy: String) {
        dialog.dismissDialog()
        selectedValue = filterBy
        binding.tvFilterType.text = when (selectedValue) {
            Constants.MOST_POPULAR -> getString(R.string.most_popular)
            else -> getString(R.string.most_recent)
        }
        getSearchedList(binding.toolbarSearch.etSearch.text.toString())
    }
}