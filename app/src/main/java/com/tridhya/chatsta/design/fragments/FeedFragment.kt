package com.tridhya.chatsta.design.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentFeedBinding
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.provider.Constants

class FeedFragment : BaseFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private var page: Int = 1
    private var gifUrl: String? = null
    private lateinit var binding: FragmentFeedBinding

    //    private val viewModel by lazy { FeedViewModel(requireContext()) }
//    private lateinit var feedAdapter: FeedAdapter
//    private var successListener: FeedAdapter.OnSuccessReactionClickListener? = null
//    private var successPostCommentListener: FeedAdapter.OnSuccessPostCommentListener? = null
    var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
//        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        if (!this::binding.isInitialized) {
            binding = FragmentFeedBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            page = 1
//            viewModel.isLoading.value = true
//            viewModel.getPosts(page = page)
//            viewModel.getUnreadNotificationsCount()
            binding.toolbar.tvMessageCount.visible()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.mainToolbar)
//        viewModel.isLoading.value = true
//        viewModel.getAllSponsorClock()
        binding.ivSponsorLogo.gone()
        binding.ivSponsorAd.gone()
        binding.ivStaticSponsor.visible()

        binding.toolbar.ivSearch.setOnClickListener(this)
        binding.toolbar.ivNotification.setOnClickListener(this)
        binding.rvFeed.layoutManager = LinearLayoutManager(requireContext())
        setObservers()
        binding.swipeRefresh.setOnRefreshListener(this)
    }

    private fun setObservers() {

//        viewModel.isRefreshing.observe(viewLifecycleOwner) {
//            binding.swipeRefresh.isRefreshing = it
//        }
        /* viewModel.responseUnreadNotificationCount.observe(viewLifecycleOwner) {
             if (it != null && it.unReadNotificationCount!! > 0) {
                 binding.toolbar.tvMessageCount.visible()
                 binding.toolbar.tvMessageCount.text = it.unReadNotificationCount.toString()
             } else {
                 binding.toolbar.tvMessageCount.gone()
             }
             if (viewModel.responseGetPosts.value != null && viewModel.responseSponsorClockItems.value != null) {
                 viewModel.isLoading.value = false
             }
         }*/

        /*        viewModel.responseSponsorClockItems.observe(viewLifecycleOwner) {
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
                    if (viewModel.responseGetPosts.value != null && viewModel.responseUnreadNotificationCount.value != null) {
                        viewModel.isLoading.value = false
                    }
                }*/

        /*  viewModel.responseDeletePosts.observe(viewLifecycleOwner) {
              viewModel.isLoading.value = false
              if (it != null) {
                  showToastShort(it)
                  viewModel.isRefreshing.value = true
                  page = 1
                  viewModel.getPosts(page = page)
                  viewModel.responseDeletePosts.value = null
              }
          }*/

        /*viewModel.responseReportPosts.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            if (it != null) {
                showToastShort(it)
                viewModel.responseReportPosts.value = null
            }
        }*/
        /*
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

                viewModel.refreshLiveData.observe(viewLifecycleOwner) {
                    page = 1
                    viewModel.isLoading.value = true
                    viewModel.getPosts(page = page)
                }*/
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

        binding.rvFeed.smoothScrollBy(0, 1)

    }

    override fun onRefresh() {
    }

}