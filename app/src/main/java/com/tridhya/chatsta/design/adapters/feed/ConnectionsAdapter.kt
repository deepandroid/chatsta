package com.tridhya.chatsta.design.adapters.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.ItemUserConnectionsBinding
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.provider.Constants.ALREADY_REQUESTED
import com.tridhya.chatsta.provider.Constants.BLOCK_USER
import com.tridhya.chatsta.provider.Constants.CONNECTED
import com.tridhya.chatsta.provider.Constants.NORMAL_USER
import com.tridhya.chatsta.provider.Constants.REQUEST_RECEIVED
import com.tridhya.chatsta.provider.Constants.UNBLOCK_USER
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.LoadMoreRecyclerViewAdapter

class ConnectionsAdapter(
    private val list: ArrayList<User> = arrayListOf(),
    private val listener: OnItemClickListener,
    val loadMoreListener: OnLoadMoreListener? = null,
    private val isCard: Boolean = true,
) :
    LoadMoreRecyclerViewAdapter<ConnectionsAdapter.ConnectionsViewHolder>(
        list as ArrayList<Any?>,
        loadMoreListener = loadMoreListener,
    ) {

    fun addList(list: List<User>?) {
        val prevSize = this.list.size
        val tempList = if (list.isNullOrEmpty()) arrayListOf() else list
        this.list.addAll(tempList)
        notifyItemRangeInserted(prevSize, this.list.size)
        if (tempList.size < Constants.CONNECTIONS_PAGE_SIZE)
            stopLoadingData()
        setLoaded()
    }

    fun setList(list: List<User>?) {
        clearList()
        continueLoadingData()
        val tempList = if (list.isNullOrEmpty()) arrayListOf() else list
        this.list.addAll(tempList)
        notifyDataSetChanged()
        if (tempList.size < Constants.CONNECTIONS_PAGE_SIZE)
            stopLoadingData()
        setLoaded()
    }

    fun clearList() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun updateConnectionStatus(position: Int, statusType: Int) {
        this.list[position].userConnectionStatus = statusType
        notifyItemChanged(position)
    }

    fun updateBlockUnblockStatus(position: Int, statusType: Int) {
        this.list[position].isBlockedConnection = statusType
        notifyItemChanged(position)
    }

    class ConnectionsViewHolder(
        val binding: ItemUserConnectionsBinding,
        val listener: OnItemClickListener,
        val mContext: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: User, isCard: Boolean, position: Int) {
            when (data.userConnectionStatus) {
                CONNECTED -> {
                    binding.ivChat.visible()
                    binding.ivSendRequest.gone()
                    binding.ivAcceptRequest.gone()
                }

                ALREADY_REQUESTED -> {
                    if (data.isAccount == false) {
                        binding.ivChat.visible()
                        binding.ivSendRequest.gone()
                        binding.ivAcceptRequest.gone()
                    } else {
                        binding.ivChat.gone()
                        binding.ivSendRequest.gone()
                        binding.ivAcceptRequest.gone()
                    }
                }

                NORMAL_USER -> {
                    binding.ivChat.gone()
                    binding.ivSendRequest.visible()
                    binding.ivAcceptRequest.gone()
                }

                REQUEST_RECEIVED -> {
                    binding.ivChat.gone()
                    binding.ivSendRequest.gone()
                    binding.ivAcceptRequest.visible()
                }
            }

            when (data.isBlockedConnection) {
                BLOCK_USER -> {
                    binding.ivMoreOptions.setImageResource(R.drawable.ic_block_red)
                }

                UNBLOCK_USER -> {
                    binding.ivMoreOptions.setImageResource(R.drawable.ic_more_info)
                }
            }

            if (!isCard) {
                binding.cvMain.cardElevation = 0f
                binding.cvMain.useCompatPadding = false
                binding.llMain.setPadding(0, 8, 0, 8)
            }

            if (data.images?.isEmpty() == false)
                GlideUtils(binding.ivProfileImage.context).circleImage()
                    .loadImage(data.images!![0].image, binding.ivProfileImage)
            else
                GlideUtils(binding.ivProfileImage.context).circleImage()
                    .loadImage(R.drawable.ic_app, binding.ivProfileImage)
            binding.tvUserName.text = data.username
            binding.tvLocation.text = data.location

            binding.tvUserName.setOnClickListener {
                (mContext as ActivityBase).preventDoubleClick(it)
                listener.onProfileClicked(data)
            }

            binding.ivProfileImage.setOnClickListener {
                (mContext as ActivityBase).preventDoubleClick(it)
                listener.onProfileClicked(data)
            }

            binding.ivSendRequest.setOnClickListener {
                (mContext as ActivityBase).preventDoubleClick(it)
                listener.onRequestSend(data, position)

            }
            binding.ivMoreOptions.setOnClickListener {
                (mContext as ActivityBase).preventDoubleClick(it)
                listener.onMoreOptions(
                    data,
                    data.isBlockedConnection == BLOCK_USER, position
                )
            }
            binding.ivChat.setOnClickListener {
                (mContext as ActivityBase).preventDoubleClick(it)
                listener.onChatSelected(data, it)
            }
            binding.ivAcceptRequest.setOnClickListener {
                (mContext as ActivityBase).preventDoubleClick(it)
                listener.onAcceptRequestClicked(data, position)
            }
        }
    }

    interface OnItemClickListener {
        fun onMoreOptions(userData: User, isBlock: Boolean, position: Int)
        fun onRequestSend(userData: User, position: Int)
        fun onChatSelected(userData: User, view: View)
        fun onAcceptRequestClicked(userData: User, position: Int)
        fun onProfileClicked(userData: User)
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemUserConnectionsBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ConnectionsViewHolder(binding, listener, mContext = parent.context)
    }

    override fun OnBindViewHolder(holder: ConnectionsViewHolder, position: Int) {
        holder.bind(list[position], isCard, position)
    }

    override fun getCount(): Int {
        return list.size
    }
}