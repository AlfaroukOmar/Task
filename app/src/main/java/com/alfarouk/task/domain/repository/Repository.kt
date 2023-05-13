package com.alfarouk.task.domain.repository

import com.alfarouk.task.data.remote.dto.MoviesResponse
import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.util.Resource

interface Repository {

    suspend fun getOnlineMovies(page: Int): Resource<MoviesResponse>

    suspend fun getOfflineMovies(): Resource<List<MovieEntity>>

}