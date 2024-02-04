package com.alfarouk.task.presentation.ui.postlist.view

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfarouk.task.R
import com.alfarouk.task.databinding.PostListBinding
import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.presentation.ui.postlist.adpater.MoviesAdapter
import com.alfarouk.task.presentation.ui.postlist.viewmodel.PostsListViewModel
import com.alfarouk.task.util.EndlessRecyclerOnScrollListener
import com.alfarouk.task.util.ErrorType
import com.alfarouk.task.util.Resource
import com.alfarouk.task.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsListFragment : Fragment() {

    lateinit var binding: PostListBinding
    val postsListViewModel: PostsListViewModel by viewModels()
   lateinit var postsListViewModeltest: PostsListViewModel

    val adapter: MoviesAdapter by lazy { MoviesAdapter(::navigateToPostDetails) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PostListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeMovieResponse()
        setOnScrollListener()
        restoreRecyclerViewState(savedInstanceState)
        postsListViewModel.getMovies()
        return binding.root
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.postsList.layoutManager = layoutManager
        binding.postsList.adapter = adapter
    }

    private fun observeMovieResponse() {
        postsListViewModel.getMovieResponse.observe(viewLifecycleOwner) { resource ->
            handleLoadAndError(resource)
        }
    }

    private fun setOnScrollListener() {
        binding.postsList.addOnScrollListener(EndlessRecyclerOnScrollListener {
            postsListViewModel.getMovies()
        })
    }

    fun restoreRecyclerViewState(savedInstanceState: Bundle?) {
        savedInstanceState?.getParcelable<Parcelable>("recycler_view_state2")?.let { state ->
            binding.postsList.layoutManager?.onRestoreInstanceState(state)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            "recycler_view_state2",
            binding.postsList.layoutManager?.onSaveInstanceState()
        )
    }

    fun handleLoadAndError(resource: Resource<List<MovieEntity>>) {

        when (resource.status) {
            Status.SUCCESS -> {
                binding.loading.isVisible = false
                binding.txtInfo.isVisible = false
                adapter.addItems(resource.data)

            }
            Status.ERROR -> {
                // Handle error state
                handleErrorState(resource)

            }
            Status.LOADING -> {
                // Handle loading state
                binding.loading.isVisible = true
                binding.txtInfo.isVisible = false


            }
        }
    }

    fun navigateToPostDetails(movie: MovieEntity) {
        findNavController().navigate(
            R.id.postDetailsFragment,
            bundleOf("movie" to movie)
        )
    }

    fun handleErrorState(resource: Resource<List<MovieEntity>>) {
        binding.loading.isVisible = false
        binding.txtInfo.isVisible = true


        binding.txtInfo.text = when (resource.message) {
            ErrorType.UNKNOWN -> {
                ErrorType.UNKNOWN.message
            }
            ErrorType.NO_CONTENT -> {
                getString(R.string.no_saved_movie)
            }
            ErrorType.NETWORK -> {
                ErrorType.NETWORK.message
            }
            ErrorType.MSG -> {
                ErrorType.MSG.message
            }
            null -> ""
        }
    }
}
