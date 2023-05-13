package com.alfarouk.task.util

import androidx.recyclerview.widget.RecyclerView


abstract class LoadMoreAdapter<T>(
    val items: ArrayList<T> = ArrayList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var showLoadingMore = false

    override fun getItemCount(): Int = dataItemCount + if (showLoadingMore) 1 else 0

    fun addItems(newItems: List<T>?, notifyInsert: Boolean = true) {
        if (newItems == null || newItems.isEmpty()) return
        val positionStart = itemCount
        items.addAll(newItems)
        if (notifyInsert) {
            notifyItemRangeInserted(positionStart, newItems.size)
        }
    }

    private val dataItemCount: Int get() = items.size

    val isEmpty: Boolean get() = items.isEmpty()

    private val loadingMoreItemPosition: Int get() = if (showLoadingMore) itemCount - 1 else RecyclerView.NO_POSITION

    fun isLoadMorePosition(position: Int) = position >= dataItemCount || dataItemCount <= 0

    fun showLoadMore() {
        if (showLoadingMore) return
        showLoadingMore = true
        notifyItemInserted(loadingMoreItemPosition)
    }

    fun hideLoadMore() {
        if (!showLoadingMore) return
        val loadingPos = loadingMoreItemPosition
        showLoadingMore = false
        notifyItemRemoved(loadingPos)
    }

    fun clear() {
        val size = itemCount
        showLoadingMore = false
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    companion object {
        const val TYPE_LOAD_MORE = -1
    }
}