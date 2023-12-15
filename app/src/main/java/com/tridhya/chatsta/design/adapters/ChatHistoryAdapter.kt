package com.tridhya.chatsta.design.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.interfaces.chat.OnItemClick
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.ItemMessagesBinding
import com.tridhya.chatsta.model.Conversation
import java.util.Locale


class ChatHistoryAdapter : RecyclerView.Adapter<ChatHistoryViewHolder>(), Filterable {
    private var originalList: ArrayList<Conversation> = arrayListOf()
    private var list: ArrayList<Conversation> = arrayListOf()
    private var listener: OnItemClick? = null

    fun setList(list: ArrayList<Conversation>) {
        this.originalList = list
        this.list = list
        notifyDataSetChanged()
    }

    fun setListener(listener: OnItemClick?) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHistoryViewHolder {
        val mBinder: ItemMessagesBinding = DataBindingUtil.inflate(
            LayoutInflater
                .from(parent.context), R.layout.item_messages, parent, false
        )
        return ChatHistoryViewHolder(mBinder, parent.context)
    }

    override fun onBindViewHolder(holder: ChatHistoryViewHolder, position: Int) {
        holder.itemView.tag = list[position]
        holder.itemView.setOnClickListener { view ->
            if (listener != null) {
                (holder.itemView.context as ActivityBase).preventDoubleClick(view)
                listener!!.onItemClicked(view.tag)
            }
        }

        holder.bind(list[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    list = originalList
                } else {
                    val filteredList: ArrayList<Conversation> = arrayListOf()
                    for (row in originalList) {
                        if (!row.name.isNullOrEmpty()
                            && row.name?.lowercase(Locale.getDefault())?.contains(
                                charString.lowercase(
                                    Locale.getDefault()
                                )
                            )!!
                        ) {
                            filteredList.add(row)
                        }
                    }
                    list = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = list
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults,
            ) {
                list = filterResults.values as ArrayList<Conversation>
                notifyDataSetChanged()
            }
        }
    }
}