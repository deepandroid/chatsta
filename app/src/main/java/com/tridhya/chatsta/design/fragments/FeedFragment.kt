package com.tridhya.chatsta.design.fragments

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentFeedBinding
import com.tridhya.chatsta.design.adapters.feed.FeedAdapter
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.dialogs.feed.MoreOptionsBottomDialog
import com.tridhya.chatsta.design.viewModel.FeedViewModel
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.PostModel
import com.tridhya.chatsta.model.PostReactionsResponseModel
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.provider.dummyData.feed.media.Feed.getFeedPostList
import com.tridhya.chatsta.utils.LoadMoreRecyclerViewAdapter

class FeedFragment() : BaseFragment(), FeedAdapter.OnItemClickListener, View.OnClickListener,
    MoreOptionsBottomDialog.OptionSelectedListener, SwipeRefreshLayout.OnRefreshListener,
    LoadMoreRecyclerViewAdapter.OnLoadMoreListener, FeedAdapter.OnBurnPostListener, Parcelable {

    private var page: Int = 1
    private var gifUrl: String? = null
    private lateinit var binding: FragmentFeedBinding
    private val viewModel by lazy { FeedViewModel(requireContext()) }
    private lateinit var feedAdapter: FeedAdapter
    private var successListener: FeedAdapter.OnSuccessReactionClickListener? = null
    private var successPostCommentListener: FeedAdapter.OnSuccessPostCommentListener? = null
    var count = 0

    var postList: List<PostModel> = arrayListOf()

    constructor(parcel: Parcel) : this() {
        page = parcel.readInt()
        gifUrl = parcel.readString()
        count = parcel.readInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
//        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        if (!this::binding.isInitialized) {
            binding = FragmentFeedBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            page = 1
            viewModel.isLoading.value = true
//            viewModel.getPosts(page = page)
            postList = getFeedPostList()
            if (!this::feedAdapter.isInitialized) {
                feedAdapter =
                    FeedAdapter(listener = this, loadMoreListener = this, burnListener = this)
                binding.rvFeed.adapter = feedAdapter
            }
            if (postList.isEmpty() && page == 1) {
                binding.tvNoFeedFound.visible()
                binding.rvFeed.gone()
            } else {
                binding.tvNoFeedFound.gone()
                binding.rvFeed.visible()
                val list = if (postList.isEmpty()) arrayListOf() else postList
                postList.forEach {
                    it.reactions.forEach { reaction ->
                        Glide.with(requireContext()).asGif().load(reaction.gifUrl)
                    }
                }
                if (page == 1) {
                    feedAdapter.setList(list)
                } else {
                    feedAdapter.addList(list)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.mainToolbar)
        viewModel.isLoading.value = true
//        viewModel.getAllSponsorClock()

        binding.toolbar.ivSearch.setOnClickListener(this)
        binding.toolbar.ivNotification.setOnClickListener(this)
        binding.rvFeed.layoutManager = LinearLayoutManager(requireContext())
        setObservers()
        binding.swipeRefresh.setOnRefreshListener(this)
    }

    private fun setObservers() {


        viewModel.responseReportPosts.observe(viewLifecycleOwner) {
/*            viewModel.isLoading.value = false
            if (it != null) {
                showToastShort(it)
                viewModel.responseReportPosts.value = null
            }
        }

        viewModel.responseGetPosts.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            viewModel.isRefreshing.value = false
            if (!this::feedAdapter.isInitialized) {
                feedAdapter =
                    FeedAdapter(listener = this, loadMoreListener = this, burnListener = this)
                binding.rvFeed.adapter = feedAdapter
            }
            if (it.data?.postList.isNullOrEmpty() && page == 1) {
                binding.tvNoFeedFound.visible()
                binding.rvFeed.gone()
            } else {
                binding.tvNoFeedFound.gone()
                binding.rvFeed.visible()
                val list =
                    if (it.data?.postList.isNullOrEmpty()) arrayListOf() else it.data?.postList
                it.data?.postList?.forEach {
                    it.reactions.forEach { reaction ->
                        Glide.with(requireContext()).asGif().load(reaction.gifUrl)
                    }
                }
                if (page == 1) {
                    feedAdapter.setList(list)
                } else {
                    feedAdapter.addList(list)
                }
            }
            if (viewModel.responseSponsorClockItems.value != null && viewModel.responseUnreadNotificationCount.value != null) {
                viewModel.isLoading.value = false
            }
        }

        viewModel.responseReactionsModel.observe(viewLifecycleOwner) {
//            viewModel.isLoading.value = false
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
//                gifUrl?.let { it1 -> (requireContext() as MainActivity).showGIF(it1) }
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

        viewModel.refreshLiveData.observe(viewLifecycleOwner) {
            page = 1
            viewModel.isLoading.value = true
            viewModel.getPosts(page = page)
        }*/
        }
    }


    override fun onItemSelected(postData: PostModel) {
        activity?.supportFragmentManager?.let {
            MoreOptionsBottomDialog.newInstance(postData, this).show(
                it, Constants.TAG
            )
        }
    }

    override fun onCommentsSelected(
        pos: Int,
        postData: PostModel,
        isMedia: Boolean,
        commentedText: String,
        successPostCommentListener: FeedAdapter.OnSuccessPostCommentListener,
    ) {
        if (isMedia) {
            val bundle = Bundle()
            bundle.putString("postId", postData.id)
            bundle.putInt("position", pos)
            findNavController().navigate(R.id.to_User_Comments, bundle)
        } else {
            if (commentedText.isNotBlank() || commentedText.isNotEmpty()
            ) {
                viewModel.commentText.set(commentedText)
                viewModel.isLoading.value = true
                this.successPostCommentListener = successPostCommentListener
//                postData.id?.let { it1 -> viewModel.postUserComments(it1) }
            } else {
                showToastShort(getString(R.string.err_comment))
            }
        }
    }

    override fun onProfileClicked(userData: User) {
        if (session?.user?.userId == userData.userId) {
            findNavController().navigate(R.id.profile)
        } else {
            val bundle = Bundle()
            bundle.putString("userId", userData.userId)
            findNavController().navigate(R.id.to_user_profile, bundle)
        }
    }

    override fun onReactionClicked(
        data: PostReactionsResponseModel,
        postData: PostModel,
        successListener: FeedAdapter.OnSuccessReactionClickListener,
    ) {
        gifUrl = data.gifUrl
//        viewModel.isLoading.value = true
        this.successListener = successListener
//        data.id?.let { postData.id?.let { it1 -> viewModel.postReaction(postId = it1, it) } }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivSearch -> {
                findNavController().navigate(R.id.to_search)
            }

            R.id.ivNotification -> {
                findNavController().navigate(R.id.to_notifications)
            }
        }
    }

    override fun onEditSelected(dialog: MoreOptionsBottomDialog, postModel: PostModel) {
        val bundle = Bundle()
        bundle.putSerializable("postId", postModel)
        findNavController().navigate(R.id.to_create_post, bundle)
    }

    override fun onDeleteSelected(dialog: MoreOptionsBottomDialog, postModel: PostModel) {
        MessageDialog.getInstance(
            requireContext(),
            getString(R.string.delete_post_title),
            getString(R.string.delete_post_msg),
            R.style.DefaultThemeDialog
        )
            .setPositiveButtonText(R.string.delete)
            .setNegativeButton(getString(R.string.do_not_delete))
            .setListener(object : MessageDialog.ButtonListener {
                override fun onPositiveBtnClicked(dialog: MessageDialog) {
                    dialog.dismiss()
                    viewModel.isLoading.value = true
//                    postModel.id?.let { viewModel.deletePost(postId = it) }
                }
            })
            .show()
    }

    override fun onReportSelected(dialog: MoreOptionsBottomDialog, postModel: PostModel) {
        viewModel.isLoading.value = true
//        postModel.id?.let { viewModel.reportPost(postId = it) }
    }

    override fun onLoadMore() {
        ++page
//        viewModel.getPosts(page = page)
    }

    override fun onSuccessBurnPost(data: PostModel, position: Int) {
        feedAdapter.removeItem(data, position)
    }

    override fun onPause() {
        super.onPause()
        if (this::feedAdapter.isInitialized) {
            feedAdapter.stopPlayers()
        }
    }

    override fun onStop() {
        super.onStop()
        if (this::feedAdapter.isInitialized) {
            feedAdapter.stopPlayers()
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(page)
        parcel.writeString(gifUrl)
        parcel.writeInt(count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeedFragment> {
        override fun createFromParcel(parcel: Parcel): FeedFragment {
            return FeedFragment(parcel)
        }

        override fun newArray(size: Int): Array<FeedFragment?> {
            return arrayOfNulls(size)
        }
    }

    override fun onRefresh() {
    }
}