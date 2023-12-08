package com.tridhya.chatsta.design.adapters.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.ItemFeedReactionBinding
import com.tridhya.chatsta.design.MainActivity
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.invisible
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.PostModel
import com.tridhya.chatsta.model.PostReactionsResponseModel
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.NumberFormatter

class PostReactionsAdapter(
    val mContext: Context,
    private val list: List<PostReactionsResponseModel> = arrayListOf(),
    private val postData: PostModel,
    private val userData: User,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<PostReactionsAdapter.ReactionViewHolder>() {

    var itemSelected: Int? = null
    var count = 0L

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemFeedReactionBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ReactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        val data = list[position]
        if ((mContext as ActivityBase).session?.user?.userId == userData.userId || userData.isPaidContentProvider == false) {
            if (data.isFree!!) {
                holder.binding.llMain.visible()
                holder.binding.tvAmount.gone()
                GlideUtils(holder.binding.ivImage.context)
                    .loadImage(data.imageUrl, holder.binding.ivImage)
                if (data.count == 0L) {
                    holder.binding.tvCount.invisible()
                } else {
                    holder.binding.tvCount.visible()
                    holder.binding.tvCount.text =
                        data.count?.let { NumberFormatter.formatInM(it) }
                }
            } else {
                holder.binding.llMain.gone()
            }
        } else {
            GlideUtils(holder.binding.ivImage.context)
                .loadImage(data.imageUrl, holder.binding.ivImage)
            if (data.isFree!!) {
                holder.binding.tvAmount.gone()
            } else {
                holder.binding.tvAmount.visible()
                holder.binding.tvAmount.text = "$${data.price}"
            }
            if (data.count == 0L) {
                holder.binding.tvCount.invisible()
            } else {
                holder.binding.tvCount.visible()
                count = data.count!!
                holder.binding.tvCount.text = count.toString()
            }
        }

        holder.binding.llMain.setOnClickListener {
            mContext.preventDoubleClick(it)
            if (data.isFree != true) {
                itemSelected =
                    if (itemSelected == holder.bindingAdapterPosition) null else holder.bindingAdapterPosition
                notifyDataSetChanged()
            } else {
                data.gifUrl?.let { it1 -> (mContext as MainActivity).showGIF(it1) }
                val data = list[holder.bindingAdapterPosition]
                notifyDataSetChanged()

                listener.onItemSelected(data, postData,
                    object : OnSuccessPostReactionListener {
                        override fun onSuccessPostReactionClick(totalReactionsCount: Long) {
                            itemSelected = null
                            holder.binding.tvCount.visible()
                            data.count = totalReactionsCount
                            holder.binding.tvCount.text =
                                (data.count).toString()
                            notifyDataSetChanged()
                        }
                    })
            }
        }

        holder.binding.tvDonate.setOnClickListener {
            mContext.preventDoubleClick(it)
            val newData = list[holder.bindingAdapterPosition]
            notifyDataSetChanged()
            data.gifUrl?.let { it1 -> (mContext as MainActivity).showGIF(it1) }
            listener.onItemSelected(
                newData,
                postData,
                object : OnSuccessPostReactionListener {
                    override fun onSuccessPostReactionClick(totalReactionsCount: Long) {
                        itemSelected = null
                        holder.binding.tvCount.visible()
                        newData.count = totalReactionsCount
                        holder.binding.tvCount.text =
                            (newData.count).toString()
                        notifyDataSetChanged()
                    }
                })
        }

        if (itemSelected != null) {
            holder.binding.tvDonate.visibility =
                if (itemSelected == holder.bindingAdapterPosition) View.VISIBLE else View.INVISIBLE
        } else {
            holder.binding.tvDonate.gone()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onItemSelected(
            data: PostReactionsResponseModel,
            postData: PostModel,
            onSuccessReactionClick: OnSuccessPostReactionListener,
        )
    }

    interface OnSuccessPostReactionListener {
        fun onSuccessPostReactionClick(totalReactionsCount: Long)
    }

    class ReactionViewHolder(val binding: ItemFeedReactionBinding) :
        RecyclerView.ViewHolder(binding.root)
}