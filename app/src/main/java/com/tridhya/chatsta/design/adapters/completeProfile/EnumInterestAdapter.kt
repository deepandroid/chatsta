package com.tridhya.chatsta.design.adapters.completeProfile

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.ItemEnumInterestBinding
import com.tridhya.chatsta.model.AllInterestResponseModel


class EnumInterestAdapter(
    private val list: ArrayList<AllInterestResponseModel> = arrayListOf(),
    private val listener: OnItemClickListener,
    private val clickable: Boolean,
) :
    RecyclerView.Adapter<EnumInterestAdapter.EnumDataViewHolder>() {

    class EnumDataViewHolder(val context: Context, val binding: ItemEnumInterestBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun getSelectedCount(): Int {
        var count = 0
        for (item in list) {
            if (item.isSelected) count++
        }
        return count
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnumDataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemEnumInterestBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return EnumDataViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: EnumDataViewHolder, position: Int) {
        holder.binding.tvName.text = list[position].interestName
        if (clickable) {
            holder.binding.llMain.isSelected = list[position].isSelected
            if (holder.binding.llMain.isSelected)
                holder.binding.tvName.setTextColor(Color.WHITE)
            else
                holder.binding.tvName.setTextColor(Color.BLACK)
            holder.binding.llMain.setOnClickListener {
                list[position].isSelected = !list[position].isSelected
                listener.onItemSelected(list[position])
                notifyDataSetChanged()
            }
        } else {
            holder.binding.llMain.isEnabled = false
            holder.binding.llMain.isSelected = false
            holder.binding.tvName.setTextColor(holder.context.getColor(R.color.purple))
        }
    }

    interface OnItemClickListener {
        fun onItemSelected(data: AllInterestResponseModel)
    }

}