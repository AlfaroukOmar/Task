package com.alfarouk.task.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessRecyclerOnScrollListener(private val callback: () -> Unit) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layout = recyclerView.layoutManager as? LinearLayoutManager
            ?: throw RuntimeException("Only LinearLayoutManager is supported in EndlessRecyclerOnScrollListener")

        val visibleItemCount = layout.childCount
        val totalItemCount = layout.itemCount
        val firstVisibleItem = layout.findFirstVisibleItemPosition()

        if (visibleItemCount + firstVisibleItem >= totalItemCount && dy > 0) {
            callback.invoke()
        }
    }
}