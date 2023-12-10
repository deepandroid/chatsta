package com.tridhya.chatsta.design.adapters.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.ItemConnectedUsersBinding
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.LoadMoreRecyclerViewAdapter

class MyConnectionsAdapter(
    private val list: ArrayList<User> = arrayListOf(),
    private val listener: OnItemClickListener,
    loadMoreListener: OnLoadMoreListener? = null,
) : LoadMoreRecyclerViewAdapter<MyConnectionsAdapter.MyConnectionsViewHolder>(
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

    class MyConnectionsViewHolder(
        val binding: ItemConnectedUsersBinding,
        val listener: OnItemClickListener,
        val mContext: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: User) {
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
        }
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemConnectedUsersBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return MyConnectionsViewHolder(
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
    }

    override fun OnBindViewHolder(holder: MyConnectionsViewHolder, position: Int) {
        holder.bind(list[position])
    }
}