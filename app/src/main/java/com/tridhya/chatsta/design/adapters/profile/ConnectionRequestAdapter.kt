package com.tridhya.chatsta.design.adapters.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.ItemUserConnectionRequestsBinding
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.provider.Constants.ACCEPTED
import com.tridhya.chatsta.provider.Constants.DECLINED
import com.tridhya.chatsta.provider.Constants.DEFAULT
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.LoadMoreRecyclerViewAdapter

class ConnectionRequestAdapter(
    private val list: ArrayList<User> = arrayListOf(),
    private val listener: OnItemClickListener,
    loadMoreListener: OnLoadMoreListener? = null,
) : LoadMoreRecyclerViewAdapter<ConnectionRequestAdapter.ConnectionRequestViewHolder>(
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

    class ConnectionRequestViewHolder(
        val binding: ItemUserConnectionRequestsBinding,
        val listener: OnItemClickListener,
        val mContext: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: User, list: ArrayList<User>, position: Int) {
            when (data.connectionRequestStatus) {
                DEFAULT -> {
                    binding.clButtons.visible()
                    binding.tvRequestDeclined.gone()
                    binding.tvRequestAccepted.gone()
                }

                ACCEPTED -> {
                    binding.clButtons.gone()
                    binding.tvRequestDeclined.gone()
                    binding.tvRequestAccepted.visible()
                }

                DECLINED -> {
                    binding.clButtons.gone()
                    binding.tvRequestDeclined.visible()
                    binding.tvRequestAccepted.gone()
                }
            }

            if (data.images?.isEmpty() == false)
                GlideUtils(binding.ivProfileImage.context).circleImage()
                    .loadImage(data.images!![0].image, binding.ivProfileImage)
            else
                GlideUtils(binding.ivProfileImage.context).circleImage()
                    .loadImage(R.drawable.ic_app, binding.ivProfileImage)
            if (!(data.firstName.isNullOrBlank() || data.firstName.isNullOrEmpty() || data.lastName.isNullOrEmpty() || data
                    .lastName.isNullOrBlank())
            ) {
                binding.tvUserName.text = "${data.firstName}\n${data.lastName}"
            } else {
                binding.tvUserName.text = data.username
            }

            binding.llMain.setOnClickListener {
                (mContext as ActivityBase).preventDoubleClick(it)
                listener.onConnectionClicked(data)
            }

            binding.btnAccept.setOnClickListener {
                (mContext as ActivityBase).preventDoubleClick(it)
                listener.onAcceptClicked(data, object : OnRequestAccept {
                    override fun onAccept() {
                        list[position].connectionRequestStatus = ACCEPTED
                    }
                })
            }

            binding.btnDecline.setOnClickListener {
                (mContext as ActivityBase).preventDoubleClick(it)
                listener.onDeclineClicked(data, object : OnRequestDecline {
                    override fun onDecline() {
                        list[position].connectionRequestStatus = DECLINED
                    }
                })
            }
        }
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemUserConnectionRequestsBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ConnectionRequestViewHolder(
            binding,
            listener,
            mContext = parent.context
        )
    }

    override fun getCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onConnectionClicked(userData: User)
        fun onAcceptClicked(userData: User, onRequestAccept: OnRequestAccept)
        fun onDeclineClicked(userData: User, onRequestDecline: OnRequestDecline)
    }

    interface OnRequestAccept {
        fun onAccept()
    }

    interface OnRequestDecline {
        fun onDecline()
    }

    override fun OnBindViewHolder(holder: ConnectionRequestViewHolder, position: Int) {
        holder.bind(list[position], list, position)
    }
}