package com.alfarouk.task.presentation.ui.postlist.adpater

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfarouk.task.R
import com.alfarouk.task.domain.entities.MovieEntity

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titleTextView: TextView = itemView.findViewById(R.id.postTitle)

    fun bind(movie: MovieEntity, onItemClick: (MovieEntity) -> Unit) {
        titleTextView.text = movie.title
        itemView.setOnClickListener { onItemClick(movie) }
    }
}