package com.alfarouk.task.presentation.ui.postlist.viewmodel

import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alfarouk.task.data.Mapper.ToEntity
import com.alfarouk.task.data.remote.dto.MoviesResponse
import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.domain.usecase.getOfflineMoviesUseCase.GetOfflineMoviesUseCase
import com.alfarouk.task.domain.usecase.getOnlineMoviesUseCase.GetOnlineMoviesUseCase
import com.alfarouk.task.presentation.BaseViewModel
import com.alfarouk.task.util.*


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PostsListViewModel @Inject constructor(
    private val getOnlineMoviesUseCase: GetOnlineMoviesUseCase,
    private val getOfflineMoviesUseCase: GetOfflineMoviesUseCase,
    private val connectivityManager: ConnectivityManager
) : BaseViewModel() {

    private val _getMoviesResponse = MutableLiveData<Resource<List<MovieEntity>>>()
    val getMovieResponse: MutableLiveData<Resource<List<MovieEntity>>>
        get() = _getMoviesResponse

    var paginator = Paginator()

    fun getMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            if (connectivityManager.isInternetAvailable()) {
                paginator.isOnline = true
                if (paginator.canLoadNextPage()) {
                    _getMoviesResponse.postValue(Resource.loading())
                    paginator.isLoading = true
                    val response = getOnlineMoviesUseCase.load(paginator.prevLastPage?.plus(1) ?: 1)
                    if (response.status == Status.SUCCESS) {
                        response.data?.let { data ->
                            val movieEntities = data.results!!.map { it.ToEntity() }
                            val successResource = Resource.success(movieEntities)
                            _getMoviesResponse.postValue(successResource)
                            paginator.lastPage = data.totalPages
                            paginator.isPaginationEnd = data.page == data.totalPages
                            paginator.prevLastPage = data.page
                        }
                        paginator.isLoading = false
                    } else if (response.status == Status.ERROR) {

                        _getMoviesResponse.postValue(response.message?.let {
                            Resource.error(
                                emptyList(),
                                it
                            )
                        } ?: Resource.error(
                            emptyList(),
                            ErrorType.UNKNOWN
                        ))
                    }
                }
            } else if (paginator.canLoadNextPage()) {
                paginator.isOnline = false

                _getMoviesResponse.postValue(Resource.loading())

                val offlineMovies = getOfflineMoviesUseCase.load()

                if (offlineMovies.status == Status.SUCCESS)
                    _getMoviesResponse.postValue(offlineMovies)
                else if (offlineMovies.status == Status.ERROR) {
                    _getMoviesResponse.postValue(offlineMovies.message?.let {
                        Resource.error(
                            emptyList(),
                            it
                        )
                    } ?: Resource.error(
                        emptyList(),
                        ErrorType.UNKNOWN
                    ))
                }

            }
        }
    }
}
