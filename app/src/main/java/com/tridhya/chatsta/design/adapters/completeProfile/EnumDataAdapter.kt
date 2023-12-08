package com.tridhya.chatsta.design.adapters.completeProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.databinding.ItemEnumDataBinding
import com.tridhya.chatsta.model.EnumDataModel

class EnumDataAdapter(
    private val list: ArrayList<EnumDataModel> = arrayListOf(),
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<EnumDataAdapter.EnumDataViewHolder>() {

    class EnumDataViewHolder(val binding: ItemEnumDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnumDataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemEnumDataBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return EnumDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EnumDataViewHolder, position: Int) {
        holder.binding.ivImage.setImageResource(list[position].imagePath)
        holder.binding.tvName.text = list[position].text
        holder.binding.tvName.isSelected = list[position].isSelected
        holder.binding.ivImage.isSelected = list[position].isSelected

        holder.binding.llMain.setOnClickListener {
            setItemSelected(position)
            listener.onItemSelected(list[position])
            notifyDataSetChanged()
        }
    }

    private fun setItemSelected(adapterPosition: Int) {
        for (i in list.indices)
            list[i].isSelected = i == adapterPosition
    }

    interface OnItemClickListener {
        fun onItemSelected(data: EnumDataModel)
    }

}