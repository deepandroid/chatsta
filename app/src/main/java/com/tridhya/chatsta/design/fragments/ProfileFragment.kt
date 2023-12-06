package com.tridhya.chatsta.design.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.tridhya.chatsta.Model.User
import com.tridhya.chatsta.Model.response.AllInterestResponseModel
import com.tridhya.chatsta.Model.response.PostModel
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentProfileBinding
import com.tridhya.chatsta.design.dialogs.MemberShipDialog
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.dialogs.feed.MoreOptionsBottomDialog
import com.tridhya.chatsta.design.viewModel.ProfileViewModel
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.utils.RelationshipStatusData

class ProfileFragment : BaseFragment(), View.OnClickListener,
    MoreOptionsBottomDialog.OptionSelectedListener {

    private lateinit var binding: FragmentProfileBinding

    //    private lateinit var interestAdapter: EnumInterestAdapter
//    private lateinit var imagesAdapter: ImagesAdapter
    /*private val myConnectionsAdapter by lazy {
        MyConnectionsAdapter(
            listener = this,
            loadMoreListener = this
        )
    }*/
    var count = 0
    private var isRegister = false

    //    private var successPostCommentListener: FeedAdapter.OnSuccessPostCommentListener? = null
//    private val viewModel by lazy { FeedViewModel(requireContext()) }
    private val profileViewModel by lazy { ProfileViewModel(requireContext()) }

    //    private lateinit var feedAdapter: FeedAdapter
    private var page: Int = 1

    //    private var successListener: FeedAdapter.OnSuccessReactionClickListener? = null
    private var gifUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentProfileBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            val arguments = arguments
            if (arguments != null) {
                isRegister = arguments.getBoolean("isRegister")
            }
//            viewModel.isLoading.value = true
//            profileViewModel.getAllDetails(session?.user?.userId.toString())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMembershipDialog()
        var isShow = true
        var scrollRange = -1
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                isShow = true
            } else if (isShow) {
                isShow = false
            }
        })
        binding.showMoreText.setOnClickListener(this)
        binding.evEditProfile.setOnClickListener(this)
        binding.tvRequestCount.setOnClickListener(this)
        binding.llConnections.setOnClickListener(this)
        binding.rvFeed.layoutManager = LinearLayoutManager(requireContext())
        binding.rvConnections.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        setObservers()
        page = 1
    }

    private fun showMembershipDialog() {
        if (isRegister) {
            isRegister = false
            MemberShipDialog(requireContext()).show()
        }
    }

    private fun setObservers() {

        /*        profileViewModel.isDataFetched.observe(viewLifecycleOwner) {
                    if (it) {
                        //user data
                        page = 1
                        session?.user = profileViewModel.responseUserData.value
                        binding.data = profileViewModel.responseUserData.value
                        setProfileData(profileViewModel.responseUserData.value)

                        //Connections data
                        val connectionsData = profileViewModel.responseMyConnections.value
                        if (connectionsData?.connectionRequestsCount != 0 || connectionsData.connectionsCount != 0) {
                            binding.cvMyConnections.visible()
                        } else {
                            binding.cvMyConnections.gone()
                        }
                        if (connectionsData?.connectionRequestsCount != null && connectionsData.connectionRequestsCount!! > 0) {
                            binding.tvRequestCount.visible()
                            binding.tvRequestCount.text =
                                "${connectionsData.connectionRequestsCount.toString()} requests"
                        } else
                            binding.tvRequestCount.gone()

                        if (connectionsData?.connections.isNullOrEmpty()) {
                            binding.rvConnections.gone()
                            binding.tvConnectionCount.gone()
                        } else {
                            binding.rvConnections.visible()
                            binding.tvConnectionCount.visible()
                            binding.tvConnectionCount.text = connectionsData?.connectionsCount.toString()
                            binding.rvConnections.adapter = myConnectionsAdapter
                            val userList = arrayListOf<User>()
                            if (connectionsData?.connections!!.size > 5) {
                                for (i in 0..4)
                                    userList.add(connectionsData.connections!![i])
                            } else {
                                for (i in connectionsData.connections!!.indices)
                                    userList.add(connectionsData.connections!![i])
                            }
                            myConnectionsAdapter.setList(userList)
                        }

                    }
                    viewModel.isLoading.value = false
                }*/

    }

    private fun setProfileData(user: User?) {
        setLayout(user)
        setInterests(user?.interests as ArrayList<AllInterestResponseModel>)
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
        binding.executePendingBindings()
        if (!user?.images.isNullOrEmpty() && user?.images?.size!! > 0) {
            binding.tvYourPhotos.gone()
            binding.viewPagerImages.visible()
            binding.tabLayout.visible()
//            imagesAdapter = ImagesAdapter(requireContext(), user.images)
//            binding.viewPagerImages.adapter = imagesAdapter
            binding.tabLayout.setupWithViewPager(binding.viewPagerImages, true)
            binding.viewPagerImages.scrollIndicators = View.SCROLL_INDICATOR_BOTTOM
        } else {
            binding.tvYourPhotos.visible()
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

            R.id.tvRequestCount, R.id.llConnections -> {
                preventDoubleClick(view)
                findNavController().navigate(R.id.to_my_connections)
            }

        }
    }

    override fun onResume() {
        super.onResume()
//        viewModel.isLoading.value = true
//        profileViewModel.getAllDetails(session?.user?.userId.toString())
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            Constants.TAG
        )?.observe(viewLifecycleOwner) {
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(Constants.TAG)
            if (it) {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
//                        feedAdapter.stopPlayers()
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
//        viewModel.isLoading.value = true
//        postModel.id?.let { viewModel.reportPost(postId = it) }
    }

}
