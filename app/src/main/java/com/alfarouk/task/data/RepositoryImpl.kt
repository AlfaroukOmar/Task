package com.alfarouk.task.data

import com.alfarouk.task.data.Mapper.ToEntity
import com.alfarouk.task.data.local.MovieDao
import com.alfarouk.task.data.remote.MovieApi
import com.alfarouk.task.data.remote.dto.MoviesResponse
import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.domain.repository.Repository
import com.alfarouk.task.util.ErrorType
import com.alfarouk.task.util.Resource
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val postApi: MovieApi,
    private val postDao: MovieDao
) : Repository {

    override suspend fun getOnlineMovies(page: Int): Resource<MoviesResponse> {
        return try {
            val response = postApi.getMovies(page)
            val moviesResponse = response.body() ?: MoviesResponse(results = emptyList())

            // Save the list of movies to the offline cache using the DAO interface
            moviesResponse.results?.let { it ->
                postDao.insertMovies(it)
            }

            Resource.success(moviesResponse)

        } catch (e: Exception) {
            Resource.error(
                MoviesResponse(results = emptyList()),
                ErrorType.fromMessage(e.message ?: ErrorType.UNKNOWN.message)
            )
        }
    }

    override suspend fun getOfflineMovies(): Resource<List<MovieEntity>> {
        return try {
            val posts = postDao.getAllMovies().map {
                it.ToEntity()
            }
            if (posts.isNotEmpty()) {
                Resource.success(posts)
            } else {
                Resource.error(emptyList(), ErrorType.NO_CONTENT)
            }
        } catch (e: Exception) {
            Resource.error(
                emptyList(),
                ErrorType.fromMessage(e.message ?: ErrorType.UNKNOWN.message)
            )
        }
    }
}