package com.tridhya.chatsta.utils

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.databinding.ItemLoadingBinding

abstract class LoadMoreRecyclerViewAdapter<T : RecyclerView.ViewHolder>(
    private val list: ArrayList<Any?>,
    private val loadMoreListener: OnLoadMoreListener? = null,
    private val visibleThreshold: Int = 3,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 999999
    private val VIEW_TYPE_LOADING = 999998

    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var visiblePosition: MutableLiveData<Int> = MutableLiveData()

    private var continueLoading = true

    init {
        isLoading.observeForever {
            if (it) {
                list.add(null)
                notifyItemInserted(list.size - 1)
            } else {
                list.remove(null)
                notifyItemRemoved(list.size - 1)
            }
        }
    }

    open fun GetItemViewType(position: Int): Int = VIEW_TYPE_ITEM

    override fun getItemViewType(position: Int): Int {
        return if (list[position] == null) VIEW_TYPE_LOADING
        else GetItemViewType(position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (recyclerView != null) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    val rvRect = Rect()
                    recyclerView.getGlobalVisibleRect(rvRect)
                    var visible = firstPosition
                    var maxPercent = 0
                    for (i in firstPosition..lastPosition) {
                        val rowRect = Rect()
                        layoutManager.findViewByPosition(i)?.getGlobalVisibleRect(rowRect)
                        var percentFirst = 0
                        if (layoutManager.findViewByPosition(i) != null) {
                            percentFirst = if (rowRect.bottom >= rvRect.bottom) {
                                val visibleHeightFirst: Int = rvRect.bottom - rowRect.top
                                visibleHeightFirst * 100 / layoutManager.findViewByPosition(i)?.height!!
                            } else {
                                val visibleHeightFirst: Int = rowRect.bottom - rvRect.top
                                visibleHeightFirst * 100 / layoutManager.findViewByPosition(i)?.height!!
                            }
                        }
                        if (percentFirst > 100) percentFirst = 100
                        if (maxPercent < percentFirst) {
                            maxPercent = percentFirst
                            visible = i
                        }
                    }
                    visiblePosition.value = visible
                }

                if (isLoading.value != true && totalItemCount <= (lastVisibleItem + visibleThreshold) && continueLoading) {
                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMore()
                        isLoading.value = true
                    }
                }
            }
        })
    }

    abstract fun OnCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                val binding =
                    ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding, parent.context)
            }

            else -> {
                OnCreateViewHolder(parent, viewType)
            }
        }
    }

    abstract fun OnBindViewHolder(holder: T, position: Int)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LoadingViewHolder -> {

            }

            else -> {
                OnBindViewHolder(holder as T, position)
            }
        }
    }

    abstract fun getCount(): Int

    override fun getItemCount(): Int {
        return getCount()
    }

    class LoadingViewHolder(val binding: ItemLoadingBinding, val mContext: Context) :
        RecyclerView.ViewHolder(binding.root)

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    fun setLoaded() {
        isLoading.value = false
    }

    fun continueLoadingData() {
        continueLoading = true
    }

    fun stopLoadingData() {
        continueLoading = false
    }

    interface onItemVisible {
        fun isItemVisible(isVisible: Boolean)
    }

}