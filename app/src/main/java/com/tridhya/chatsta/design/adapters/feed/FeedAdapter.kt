package com.tridhya.chatsta.design.adapters.feed

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.ItemFeedTextBinding
import com.tridhya.chatsta.enum.chat.UserStatus
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.PostModel
import com.tridhya.chatsta.model.PostReactionsResponseModel
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.provider.Constants.MEDIA
import com.tridhya.chatsta.utils.DateTimeUtils.covertTimeStampToText
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.LoadMoreRecyclerViewAdapter
import java.util.concurrent.TimeUnit

class FeedAdapter(
    private val list: ArrayList<PostModel> = arrayListOf(),
    private val listener: OnItemClickListener,
    private val burnListener: OnBurnPostListener,
    val loadMoreListener: OnLoadMoreListener? = null,
) :
    LoadMoreRecyclerViewAdapter<FeedAdapter.FeedViewHolder>(
        list as ArrayList<Any?>,
        loadMoreListener = loadMoreListener
    ) {

    fun addList(list: List<PostModel>?) {
        val prevSize = this.list.size
        val tempList = if (list.isNullOrEmpty()) arrayListOf() else list
        this.list.addAll(tempList)
        notifyItemRangeInserted(prevSize, this.list.size)
        if (tempList.size < Constants.POST_PAGE_SIZE)
            stopLoadingData()
        setLoaded()
    }

    fun setList(list: List<PostModel>?) {
        clearList()
        continueLoadingData()
        val tempList = if (list.isNullOrEmpty()) arrayListOf() else list
        this.list.addAll(tempList)
        notifyDataSetChanged()
        if (tempList.size < Constants.POST_PAGE_SIZE)
            stopLoadingData()
        setLoaded()
    }

    fun changeCommentCount(position: Int, count: Int) {
        list[position].commentCount = count
        notifyDataSetChanged()
    }

    fun clearList() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun removeItem(data: PostModel, position: Int) {
        this.list.remove(data)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }

    class FeedViewHolder(
        val binding: ItemFeedTextBinding,
        val listener: OnItemClickListener,
        val burnListener: OnBurnPostListener,
        val mContext: Context,
    ) :
        RecyclerView.ViewHolder(binding.root), PostReactionsAdapter.OnItemClickListener {
        private var mediaAdapter: PostMediaAdapter? = null
        private var reactionsAdapter: PostReactionsAdapter? = null
        private var timer: CountDownTimer? = null
        var successListener: PostReactionsAdapter.OnSuccessPostReactionListener? = null
        private var itemVisiblePosition: MutableLiveData<Int?> = MutableLiveData(null)
        var currentItem: MutableLiveData<Int?> = MutableLiveData(0)

        //        private var mDBReference: DatabaseReference? = null
        private var lastSeen: Long = 0
        private var isOnline: UserStatus = UserStatus.OFFLINE
        private var userSetStatus: UserStatus = UserStatus.ONLINE

        @SuppressLint("ClickableViewAccessibility")
        fun bind(
            data: PostModel,
            position: Int,
            visiblePosition: MutableLiveData<Int>,
        ) {
            val userData = data.postedBy
            val reactionList = data.reactions
//            mDBReference = AppClass.mDBReference

            /*            mDBReference //Observing user
                            ?.child(Constants.TABLE_USERS)?.child(userData?.userId.toString())
                            ?.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val values = dataSnapshot.value as HashMap<*, *>?
                                    Log.e("DataSnapshot", dataSnapshot.value.toString())

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
                                                if (getMessageIdChatWithMe(userData?.userId.toString()) == inChatWith) {
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
                            })*/

            if (timer != null) {
                timer!!.cancel()
            }
            if (!(data.burnTimestamp.isNullOrBlank() || data.burnTimestamp.isNullOrEmpty()) && !(data.burnTimestamp48hours.isNullOrBlank() || data.burnTimestamp48hours.isNullOrEmpty())) {
                if (data.burnTimestamp48hours!!.toLong() > System.currentTimeMillis()) {
                    binding.llBurn.visible()
                    binding.ivBurnGif.gone()
                    timer = object : CountDownTimer(
                        (data.burnTimestamp48hours!!.toLong() - System.currentTimeMillis()),
                        1000
                    ) {
                        override fun onTick(duration: Long) {
                            binding.tvBurnTime.text = String.format(
                                "%02d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(duration) % 60,
                                TimeUnit.MILLISECONDS.toMinutes(duration) % 60,
                                TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                            )

                        }

                        override fun onFinish() {
                            binding.llBurn.gone()
                            binding.ivBurnGif.visible()
                            showGIF(
                                mContext,
                                R.raw.fire_flood_animation,
                                binding.ivBurnGif,
                                data,
                                position,
                                burnListener
                            )
                        }

                    }.start()
                } else {
                    binding.llBurn.gone()
                    binding.ivBurnGif.visible()
                    showGIF(
                        mContext,
                        R.raw.fire_flood_animation,
                        binding.ivBurnGif,
                        data,
                        position,
                        burnListener
                    )
                }
            }

            binding.recyclerviewReactions.layoutManager =
                LinearLayoutManager(
                    mContext,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            binding.recyclerviewReactions.isNestedScrollingEnabled = false
            reactionsAdapter =
                if ((mContext as ActivityBase).session?.user?.userId == userData?.userId || userData?.isPaidContentProvider == false) {
                    val list = arrayListOf<PostReactionsResponseModel>()
                    for (i in reactionList.indices) {
                        if (reactionList[i].isFree == true) {
                            list.add(reactionList[i])
                        }
                    }
                    userData?.let { PostReactionsAdapter(mContext, list, data, it, this) }
                } else {
                    userData?.let { PostReactionsAdapter(mContext, reactionList, data, it, this) }
                }
            binding.recyclerviewReactions.adapter = reactionsAdapter
            if (userData != null) {
                if (userData.images?.isEmpty() == false) {
                    GlideUtils(binding.ivProfileImage.context).circleImage()
                        .loadImage(userData.images!![0].image, binding.ivProfileImage)
                } else {
                    GlideUtils(binding.ivProfileImage.context).circleImage()
                        .loadImage(R.drawable.ic_app, binding.ivProfileImage)
                }
                binding.tvUserName.text = userData.username
                if (data.postedBy!!.isPaidContentProvider == true) {
                    binding.llDonation.visible()
                    binding.tvDonationAmount.text = data.totalDonation.toString()
                } else {
                    binding.llDonation.gone()
                }
            }
            binding.tvTime.text = covertTimeStampToText(data.burnTimestamp)
            binding.tvCommentsCount.text = if (data.commentCount!! < 2)
                data.commentCount.toString() + " " + mContext.getString(R.string.comment)
            else
                data.commentCount.toString() + " " + mContext.getString(R.string.comments)
            if (data.text.isNullOrBlank() || data.text.isNullOrEmpty())
                binding.tvText.visibility = View.GONE
            else {
                binding.tvText.visibility = View.VISIBLE
                binding.tvText.text = data.text
            }
            binding.recyclerviewMedia.layoutManager =
                LinearLayoutManager(
                    mContext,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            if (data.postType?.equals(MEDIA) == true && data.medias.isNotEmpty()) {
                binding.recyclerviewMedia.visible()
                binding.llCurrentItem.visible()
                binding.recyclerviewMedia.isNestedScrollingEnabled = false
                mediaAdapter = PostMediaAdapter(data.medias, itemVisiblePosition)
                val helper: SnapHelper = PagerSnapHelper()
                if (binding.recyclerviewMedia.onFlingListener == null)
                    helper.attachToRecyclerView(binding.recyclerviewMedia)
                binding.recyclerviewMedia.adapter = mediaAdapter
                binding.tvTotalItem.text = data.medias.size.toString()
                if (data.medias.size < 2)
                    binding.llCurrentItem.gone()
                else
                    binding.llCurrentItem.visible()
                binding.tvCurrentItem.text = "1"
                binding.recyclerviewMedia.addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        val position: Int = getCurrentItem(binding.recyclerviewMedia)
                        binding.tvCurrentItem.text = (position + 1).toString()
                        currentItem.value = position
                    }
                })

            } else {
                binding.recyclerviewMedia.gone()
                binding.llCurrentItem.gone()
            }

            binding.evComments.setOnTouchListener { view, event ->
                if (view.id == R.id.evComments) {
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(
                            false
                        )
                    }
                }
                false
            }

            binding.tvUserName.setOnClickListener {
                mContext.preventDoubleClick(it)
                userData?.let { it1 -> listener.onProfileClicked(it1) }
            }
            binding.ivProfileImage.setOnClickListener {
                mContext.preventDoubleClick(it)
                userData?.let { it1 -> listener.onProfileClicked(it1) }
            }
            binding.ivMoreOptions.setOnClickListener {
                mContext.preventDoubleClick(it)
                listener.onItemSelected(data)
            }
            binding.ivAddComments.setOnClickListener {
                mContext.preventDoubleClick(it)
                listener.onCommentsSelected(position, data, isMedia = true, "",
                    object : OnSuccessPostCommentListener {
                        override fun onSuccessPostComment(totalCount: Int) {
                        }
                    })
            }
            binding.llCommentsCount.setOnClickListener {
                mContext.preventDoubleClick(it)
                listener.onCommentsSelected(position, data, isMedia = true, "",
                    object : OnSuccessPostCommentListener {
                        override fun onSuccessPostComment(totalCount: Int) {
                        }
                    })
            }
            binding.ivPostComments.setOnClickListener {
                mContext.preventDoubleClick(it)
                listener.onCommentsSelected(
                    position,
                    data,
                    isMedia = false,
                    binding.evComments.text.toString(), object : OnSuccessPostCommentListener {
                        override fun onSuccessPostComment(totalCount: Int) {
//                            data.commentCount = totalCount
                            binding.evComments.setText("")
                            binding.tvCommentsCount.text = if (totalCount < 2)
                                totalCount.toString() + " " + mContext.getString(R.string.comment)
                            else
                                totalCount.toString() + " " + mContext.getString(R.string.comments)
                        }
                    }
                )
            }

            visiblePosition.observeForever {
                itemVisiblePosition.value = if (it == bindingAdapterPosition) {
                    currentItem.value
                } else {
                    null
                }
            }

            currentItem.observeForever {
                itemVisiblePosition.value = if (visiblePosition.value == bindingAdapterPosition) {
                    currentItem.value
                } else {
                    null
                }
            }
        }

        fun getMessageIdChatWithMe(userId: String): String {
            val myUserId = (mContext as ActivityBase).session?.user?.userId.toString()
            return if (myUserId < userId) {
                "${myUserId}_${userId}"
            } else {
                "${userId}_${myUserId}"
            }
        }

        fun showGIF(
            context: Context,
            url: Int,
            view: ImageView,
            data: PostModel,
            position: Int,
            burnListener: OnBurnPostListener,
        ) {
            view.visible()
            Glide.with(context).asGif().load(url)
                .listener(object : RequestListener<GifDrawable?> {
                    override fun onResourceReady(
                        resource: GifDrawable?,
                        model: Any?,
                        target: Target<GifDrawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        resource?.setLoopCount(1)
                        resource?.registerAnimationCallback(object :
                            Animatable2Compat.AnimationCallback() {
                            override fun onAnimationEnd(drawable: Drawable) {
                                view.gone()
                                burnListener.onSuccessBurnPost(data, position)
                            }
                        })
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<GifDrawable?>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        return false
                    }
                }).into(view)

        }

        fun getCurrentItem(view: RecyclerView): Int {
            return (view.layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
        }

        override fun onItemSelected(
            data: PostReactionsResponseModel,
            postData: PostModel,
            reactionSuccessListener: PostReactionsAdapter.OnSuccessPostReactionListener,
        ) {
            listener.onReactionClicked(data, postData, object : OnSuccessReactionClickListener {
                override fun onSuccessReactionClick(
                    totalDonation: Double?,
                    totalReactionsCount: Long,
                ) {
                    if (binding.llDonation.isVisible) {
                        binding.tvDonationAmount.text = totalDonation.toString()
                    }
                    successListener = reactionSuccessListener
                    successListener?.onSuccessPostReactionClick(totalReactionsCount)
                }
            })
        }

    }

    interface OnSuccessReactionClickListener {
        fun onSuccessReactionClick(totalDonation: Double?, totalReactionsCount: Long)
    }

    interface OnSuccessPostCommentListener {
        fun onSuccessPostComment(totalCount: Int)
    }

    interface OnBurnPostListener {
        fun onSuccessBurnPost(data: PostModel, position: Int)
    }

    interface OnItemClickListener {
        fun onItemSelected(postData: PostModel)
        fun onCommentsSelected(
            position: Int,
            postData: PostModel,
            isMedia: Boolean,
            commentedText: String,
            successListener: OnSuccessPostCommentListener,
        )

        fun onProfileClicked(userData: User)
        fun onReactionClicked(
            data: PostReactionsResponseModel,
            postData: PostModel,
            successListener: OnSuccessReactionClickListener,
        )
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemFeedTextBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return FeedViewHolder(
            binding,
            listener,
            mContext = parent.context,
            burnListener = burnListener,
        )
    }

    override fun OnBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(list[position], position, visiblePosition)
    }

    override fun getCount(): Int {
        return list.size
    }

    fun stopPlayers() {
        visiblePosition.value = null
    }

}