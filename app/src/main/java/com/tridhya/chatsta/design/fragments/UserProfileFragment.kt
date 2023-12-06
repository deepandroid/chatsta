package com.tridhya.chatsta.design.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.tridhya.chatsta.Model.User
import com.tridhya.chatsta.Model.response.AllInterestResponseModel
import com.tridhya.chatsta.Model.response.PostModel
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentUserProfileBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.dialogs.feed.ConnectionsMoreOptionsBottomDialog
import com.tridhya.chatsta.design.dialogs.feed.MoreOptionsBottomDialog
import com.tridhya.chatsta.design.viewModel.ProfileViewModel
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.invisible
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.provider.Constants.ALREADY_REQUESTED
import com.tridhya.chatsta.provider.Constants.BLOCK_USER
import com.tridhya.chatsta.provider.Constants.CONNECTED
import com.tridhya.chatsta.provider.Constants.NORMAL_USER
import com.tridhya.chatsta.provider.Constants.REQUEST_RECEIVED
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.RelationshipStatusData

class UserProfileFragment : BaseFragment(), MoreOptionsBottomDialog.OptionSelectedListener,
    View.OnClickListener,
    ConnectionsMoreOptionsBottomDialog.OptionSelectedListener {

    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var userData: User
    private var userId: String? = null
    private var responseUser: User? = null
    var count = 0
    var isBlock = false

    //    private var mDBReference: DatabaseReference? = null
//    private lateinit var interestAdapter: EnumInterestAdapter
//    private lateinit var imagesAdapter: ImagesAdapter
//    private val viewModel by lazy { FeedViewModel(requireContext()) }
    private val profileViewModel by lazy { ProfileViewModel(requireContext()) }

    //    private lateinit var feedAdapter: FeedAdapter
    private var page: Int = 1

    //    private var successListener: FeedAdapter.OnSuccessReactionClickListener? = null
    private var gifUrl: String? = null
//    private var successPostCommentListener: FeedAdapter.OnSuccessPostCommentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentUserProfileBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            val arguments = arguments
            if (arguments != null) {
                userId = arguments.getString("userId")
            }
//            viewModel.isLoading.value = true
//            userId?.let { profileViewModel.getUserProfileDetails(it) }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mDBReference = AppClass.mDBReference
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = ""
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.showMoreText.setOnClickListener(this)
        binding.evEditProfile.setOnClickListener(this)
        binding.btnConnect.setOnClickListener(this)
        binding.btnMessage.setOnClickListener(this)
        binding.btnPublicConnect.setOnClickListener(this)
        binding.btnPublicMessage.setOnClickListener(this)
        binding.ivMoreOptions.setOnClickListener(this)
        binding.ivPublicMoreOptions.setOnClickListener(this)
        binding.rvFeed.layoutManager = LinearLayoutManager(requireContext())
        setObservers()
        page = 1
    }

    private fun setPrivateProfileView(user: User) {
        binding.rlMyProfile.gone()
        binding.cvMyPosts.gone()
        binding.clPrivateProfile.visible()
        when (user.userConnectionStatus) {
            NORMAL_USER -> {
                binding.btnConnect.visible()
                binding.btnConnect.isEnabled = true
                binding.btnConnect.text = getString(R.string.connect)
                binding.btnMessage.gone()
            }

            CONNECTED -> {
                binding.btnConnect.gone()
                binding.btnMessage.visible()
            }

            ALREADY_REQUESTED -> {
                binding.btnConnect.visible()
                binding.btnConnect.isEnabled = false
                binding.btnConnect.text = getString(R.string.requested)
                binding.btnMessage.gone()
            }

            REQUEST_RECEIVED -> {
                binding.btnConnect.visible()
                binding.btnConnect.isEnabled = true
                binding.btnConnect.text = getString(R.string.accept)
                binding.btnMessage.gone()
            }
        }
        if (user.isBlockedConnection == BLOCK_USER) {
            binding.ivMoreOptions.setImageResource(R.drawable.ic_block_red)
        } else {
            binding.ivMoreOptions.setImageResource(R.drawable.ic_more_info)
        }
        if (!user.images.isNullOrEmpty() && user.images.isNotEmpty()) {
            binding.tvYourPhotos.gone()
            binding.viewPagerImages.gone()
            binding.tabLayout.gone()
            binding.ivProfileImage.visible()
            GlideUtils(requireContext()).loadImage(
                user.images[0].image,
                binding.ivProfileImage
            )
        } else {
            binding.tvYourPhotos.visible()
            binding.viewPagerImages.gone()
            binding.tabLayout.gone()
            binding.ivProfileImage.gone()
        }
    }

    private fun setProfileView(user: User) {
        if (user.userId == session?.user?.userId) {
            binding.evEditProfile.visible()
            binding.evProfileEmpty.invisible()
            binding.clButtons.gone()
        } else {
            binding.evEditProfile.invisible()
            binding.evProfileEmpty.invisible()
            binding.clButtons.visible()
        }
        when (user.userConnectionStatus) {
            NORMAL_USER -> {
                binding.btnPublicConnect.visible()
                binding.btnPublicConnect.isEnabled = true
                binding.btnPublicConnect.text = getString(R.string.connect)
                binding.btnPublicMessage.visible()
            }

            CONNECTED -> {
                binding.btnPublicConnect.gone()
                binding.btnPublicMessage.visible()
            }

            ALREADY_REQUESTED -> {
                binding.btnPublicConnect.visible()
                binding.btnPublicConnect.isEnabled = false
                binding.btnPublicConnect.text = getString(R.string.requested)
                binding.btnPublicMessage.visible()
            }

            REQUEST_RECEIVED -> {
                binding.btnPublicConnect.visible()
                binding.btnPublicConnect.isEnabled = true
                binding.btnPublicConnect.text = getString(R.string.accept)
                binding.btnPublicMessage.visible()
            }
        }
        if (user.isBlockedConnection == BLOCK_USER) {
            binding.ivPublicMoreOptions.setImageResource(R.drawable.ic_block_red)
        } else {
            binding.ivPublicMoreOptions.setImageResource(R.drawable.ic_more_info)
        }
        binding.cvMyPosts.visible()
        binding.rlMyProfile.visible()
        binding.clPrivateProfile.gone()
        setLayout(user)
        setInterests(user.interests as ArrayList<AllInterestResponseModel>)
        val genderList = RelationshipStatusData.getGenderList(requireContext())
        val relationshipList = RelationshipStatusData.getRelationshipList(requireContext())
        val starSignList = RelationshipStatusData.getStarSignList(requireContext())
        for (i in genderList.indices) {
            if (user.gender.equals(genderList[i].enum, true)) {
                binding.ivGenderImage.setImageResource(genderList[i].imagePath)
                binding.ivGenderImage.isSelected = true
                binding.tvGenderName.isSelected = true
                binding.tvGenderName.text = genderList[i].text
            }
        }
        for (i in relationshipList.indices) {
            if (user.relationShipStatus.equals(relationshipList[i].enum, true)) {
                binding.ivRelationshipStatusImage.setImageResource(relationshipList[i].imagePath)
                binding.ivRelationshipStatusImage.isSelected = true
                binding.tvRelationshipStatusName.isSelected = true
                binding.tvRelationshipStatusName.text = relationshipList[i].text
            }
        }
        for (i in starSignList.indices) {
            if (user.starSign.equals(starSignList[i].text, true)) {
                binding.ivStarSignImage.setImageResource(starSignList[i].imagePath)
                binding.ivStarSignImage.isSelected = true
                binding.tvStarSignName.isSelected = true
                binding.tvStarSignName.text = starSignList[i].text
            }
        }
    }

    private fun setLayout(user: User?) {
        if (!user?.images.isNullOrEmpty() && user?.images?.size!! > 0) {
            binding.tvYourPhotos.gone()
            binding.ivProfileImage.gone()
            binding.viewPagerImages.visible()
            binding.tabLayout.visible()
//            imagesAdapter = ImagesAdapter(requireContext(), user.images)
//            binding.viewPagerImages.adapter = imagesAdapter
            binding.tabLayout.setupWithViewPager(binding.viewPagerImages, true)
            binding.viewPagerImages.scrollIndicators = View.SCROLL_INDICATOR_BOTTOM
        } else {
            binding.tvYourPhotos.visible()
            binding.ivProfileImage.gone()
            binding.viewPagerImages.gone()
            binding.tabLayout.gone()
        }

        binding.address.text = if (user?.location.isNullOrBlank()) {
            getString(R.string.not_selected)
        } else {
            user?.location
        }
        if (user?.gender.isNullOrBlank())
            binding.llGender.visibility = View.GONE
        else
            binding.llGender.visibility = View.VISIBLE
        if (user?.relationShipStatus.isNullOrBlank())
            binding.llRelationshipStatus.visibility = View.GONE
        else
            binding.llRelationshipStatus.visibility = View.VISIBLE
        if (user?.starSign.isNullOrBlank())
            binding.llStarSign.visibility = View.GONE
        else
            binding.llStarSign.visibility = View.VISIBLE
        if (user?.bio.isNullOrBlank())
            binding.bio.visibility = View.GONE
        else
            binding.bio.visibility = View.VISIBLE
        if (user?.height == null || user.height == 0)
            binding.llHeight.visibility = View.GONE
        else
            binding.llHeight.visibility = View.VISIBLE
        if (user?.interests == null || user.interests.isEmpty())
            binding.llInterests.visibility = View.GONE
        else
            binding.llInterests.visibility = View.VISIBLE
        if (user?.quotes.isNullOrBlank())
            binding.llQuotes.visibility = View.GONE
        else
            binding.llQuotes.visibility = View.VISIBLE
        if ((user?.height == null || user.height == 0) && (user?.interests == null || user.interests.isEmpty()) && user?.quotes.isNullOrBlank()) {
            binding.showMoreText.visibility = View.GONE
        } else
            binding.showMoreText.visibility = View.VISIBLE
    }

    private fun setInterests(interestList: ArrayList<AllInterestResponseModel>) {
//        interestAdapter =
//            EnumInterestAdapter(interestList, object : EnumInterestAdapter.OnItemClickListener {
//                override fun onItemSelected(data: AllInterestResponseModel) {
//                    binding.recyclerview.isSelected = interestAdapter.getSelectedCount() != 0
//                }
//            }, false)
        binding.recyclerview.layoutManager = GridLayoutManager(context, 3)
//        binding.recyclerview.adapter = interestAdapter
    }


    private fun setObservers() {

        /*        profileViewModel.isDataFetched.observe(viewLifecycleOwner) {
                    if (it) {
                        //User data
                        val data = profileViewModel.responseUserData.value
                        if (this::feedAdapter.isInitialized)
                            feedAdapter.clearList()
                        binding.data = data
                        userData = data!!
                        setToolbarTitle()
                        responseUser = data
                        if (data.userId == session?.user?.userId || data.isAccount == false || (data.isAccount == true && data.userConnectionStatus == CONNECTED)) {
                            setProfileView(data)
                            page = 1
                            viewModel.isLoading.value = true
                            viewModel.getMyPosts(data.userId.toString(), page)
                        } else {
                            setPrivateProfileView(data)
                        }
                        isBlock = (data.isBlockedConnection == BLOCK_USER)
                        val params: MutableMap<String, Any> = HashMap()
                        params["blockedBy_${session?.user?.userId}"] = isBlock

                        mDBReference
                            ?.child(Constants.TABLE_CHAT)
                            ?.child(Constants.TABLE_THREADS)
                            ?.child(getMessageIdChatWithMe(userData.userId.toString()))
                            ?.updateChildren(params)

                        //Posts
                        val postData = profileViewModel.responseGetPosts.value
                        if (!this::feedAdapter.isInitialized) {
                            feedAdapter =
                                FeedAdapter(listener = this, loadMoreListener = this, burnListener = this)
                            binding.rvFeed.adapter = feedAdapter
                        }
                        if (postData?.postList.isNullOrEmpty() && page == 1) {
                            binding.rvFeed.gone()
                        } else {
                            binding.rvFeed.visible()
                            val list =
                                if (postData?.postList.isNullOrEmpty()) arrayListOf() else postData?.postList
                            if (page == 1) {
                                feedAdapter.setList(list)
                            } else {
                                feedAdapter.addList(list)
                            }
                        }
                    }
                    viewModel.isLoading.value = false
                }

                viewModel.responseDeletePosts.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    if (it != null) {
                        showToastShort(it)
                        viewModel.isRefreshing.value = true
                        page = 1
                        viewModel.getMyPosts(userData.userId.toString(), page = page)
                        viewModel.responseDeletePosts.value = null
                    }
                }

                viewModel.responseReportPosts.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    if (it != null) {
                        showToastShort(it)
                        viewModel.responseReportPosts.value = null
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
                    viewModel.isLoading.value = true
                    userId?.let { profileViewModel.getUserProfileDetails(it) }        }

                viewModel.responseCancelConnectionRequest.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    viewModel.isLoading.value = true
                    userId?.let { profileViewModel.getUserProfileDetails(it) }
                }

                viewModel.responseSendConnectionRequest.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    viewModel.isLoading.value = true
                    userId?.let { profileViewModel.getUserProfileDetails(it) }
                }

                viewModel.responseBlockUnblockConnection.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    viewModel.isLoading.value = true
                    userId?.let { profileViewModel.getUserProfileDetails(it) }
                }

                viewModel.responseDeclineConnectionRequest.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    viewModel.isLoading.value = true
                    userId?.let { profileViewModel.getUserProfileDetails(it) }
                }

                viewModel.responseAcceptConnectionRequest.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    viewModel.isLoading.value = true
                    userId?.let { profileViewModel.getUserProfileDetails(it) }
                }*/

//        viewModel.refreshLiveData.observe(viewLifecycleOwner) {
//            page = 1
//            getSearchedList(binding.toolbarSearch.etSearch.text.toString())
//        }
    }

    private fun setToolbarTitle() {
        var isShow = true
        var scrollRange = -1
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                context?.getColor(R.color.black)
                    ?.let { binding.collapsingToolbar.setCollapsedTitleTextColor(it) }
                context?.getColor(R.color.black)
                    ?.let { binding.collapsingToolbar.setExpandedTitleColor(it) }
                binding.collapsingToolbar.title = userData.username

                isShow = true
            } else if (isShow) {
                (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
                binding.collapsingToolbar.title = ""
                isShow = false
            }
        })
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
//                            feedAdapter.stopPlayers()
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
//                    viewModel.isLoading.value = true
//                    postModel.id?.let { viewModel.deletePost(postId = it) }
                }
            })
            .show()
    }

    override fun onReportSelected(dialog: MoreOptionsBottomDialog, postModel: PostModel) {
        /*        viewModel.isLoading.value = true
                postModel.id?.let { viewModel.reportPost(postId = it) }*/
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.show_more_text -> {
                preventDoubleClick(view)
                if (binding.showMoreLayout.visibility == View.GONE) {
                    binding.showMoreLayout.visibility = View.VISIBLE
                    binding.showMoreText.text = getString(R.string.show_less)
                } else {
                    binding.showMoreLayout.visibility = View.GONE
                    binding.showMoreText.text = getString(R.string.show_more)
                }
            }

            R.id.evEditProfile -> {
                preventDoubleClick(view)
                findNavController().navigate(R.id.to_edit_profile)
            }

            R.id.btnConnect, R.id.btnPublicConnect -> {
                preventDoubleClick(view)
                /*viewModel.isLoading.value = true
                if (responseUser?.userConnectionStatus == REQUEST_RECEIVED) {
                    userData.userId?.let { viewModel.acceptRequestConnection(userId = it) }
                } else {
                    userData.userId?.let { viewModel.requestConnection(userId = it) }
                }*/
            }

            R.id.btnMessage, R.id.btnPublicMessage -> {
                preventDoubleClick(view)
                val bundle = Bundle()
                bundle.putString("userId", userData.userId)
                findNavController().navigate(R.id.to_chat, bundle)
            }

            R.id.ivMoreOptions, R.id.ivPublicMoreOptions -> {
                activity?.supportFragmentManager?.let {
                    responseUser?.let { it1 ->
                        ConnectionsMoreOptionsBottomDialog.newInstance(
                            it1,
                            responseUser!!.isBlockedConnection == BLOCK_USER,
                            this
                        ).show(
                            it, Constants.TAG
                        )
                    }
                }
                preventDoubleClick(view)
            }
        }
    }

    /*  override fun onSuccessBurnPost(data: PostModel, position: Int) {
          feedAdapter.removeItem(data, position)
      }*/

    override fun onBlockSelected(dialog: ConnectionsMoreOptionsBottomDialog, userData: User) {
//        viewModel.isLoading.value = true
//        userData.userId?.let { viewModel.blockUnblockConnection(userId = it) }
    }

    override fun onUnblockSelected(dialog: ConnectionsMoreOptionsBottomDialog, userData: User) {
//        viewModel.isLoading.value = true
//        userData.userId?.let { viewModel.blockUnblockConnection(userId = it) }
    }

    override fun onDisconnectSelected(dialog: ConnectionsMoreOptionsBottomDialog, userData: User) {
        when (userData.userConnectionStatus) {
            ALREADY_REQUESTED -> {
//                viewModel.isLoading.value = true
//                userData.userId?.let { viewModel.cancelConnectionRequest(userId = it) }
            }

            REQUEST_RECEIVED -> {
//                viewModel.isLoading.value = true
//                userData.userId?.let { viewModel.deleteRequestConnection(userId = it) }
            }

            else -> {
//                viewModel.isLoading.value = true
//                userData.userId?.let { viewModel.disconnectConnection(userId = it) }
            }
        }
    }

    /* override fun onPause() {
         super.onPause()
         if (this::feedAdapter.isInitialized)
             feedAdapter.stopPlayers()
     }

     override fun onStop() {
         super.onStop()
         if (this::feedAdapter.isInitialized)
             feedAdapter.stopPlayers()
     }*/
}