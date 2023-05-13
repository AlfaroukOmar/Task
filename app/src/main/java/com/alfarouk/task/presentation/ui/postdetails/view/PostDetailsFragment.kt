package com.alfarouk.task.presentation.ui.postdetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alfarouk.task.databinding.PostDetailsBinding
import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.util.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : Fragment() {

    private var movie: MovieEntity? = null
    lateinit var binding: PostDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = PostDetailsBinding.inflate(inflater, container, false)


        arguments?.let {
            movie = it.getSerializable("movie", MovieEntity::class.java)
        }

        if (movie != null) {
            binding.movieTitle.text = movie!!.title
            binding.movieImage.loadImage(movie!!.imageUrl)
        }

        return binding.root
    }
}