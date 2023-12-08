package com.tridhya.chatsta.design.fragments.completeProfile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.ItemLocationPlacesBinding


class PlacesAutoCompleteAdapter(val listener: onItemClick) :
    RecyclerView.Adapter<PlacesAutoCompleteAdapter.PlacesAutoCompleteViewHolder>() {

    class PlacesAutoCompleteViewHolder(
        val context: Context,
        val binding: ItemLocationPlacesBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlacesAutoCompleteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemLocationPlacesBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return PlacesAutoCompleteViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: PlacesAutoCompleteViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onItemClicked(holder.binding.root.context.getString(R.string.gold_coast_queensland))
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    interface onItemClick {
        fun onItemClicked(result: String)
    }

}