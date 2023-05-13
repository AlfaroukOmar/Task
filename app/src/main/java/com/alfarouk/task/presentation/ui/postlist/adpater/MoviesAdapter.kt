package com.alfarouk.task.presentation.ui.postlist.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfarouk.task.R
import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.util.LoadMoreAdapter

class MoviesAdapter(
    private val onItemClick: (MovieEntity) -> Unit
) : LoadMoreAdapter<MovieEntity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            val movie = items[position]
            holder.bind(movie, onItemClick)
        }
    }

}