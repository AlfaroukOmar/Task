package com.alfarouk.task.domain.usecase.getOnlineMoviesUseCase

import com.alfarouk.task.data.remote.dto.MoviesResponse
import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.domain.repository.Repository
import com.alfarouk.task.util.ErrorType
import com.alfarouk.task.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetOnlineMoviesUseCase @Inject constructor(private val repository: Repository) {
    suspend fun load(page: Int = 1): Resource<MoviesResponse> {
        lateinit var result: Resource<MoviesResponse>
        result(page) { result = it }
        return result
    }

    private suspend fun result(
        page: Int,
        res: (Resource<MoviesResponse>) -> Unit
    ) {
        flow {
            emit(repository.getOnlineMovies(page))
        }.flowOn(Dispatchers.IO)
            .catch {
                it.message?.let {
                    Resource.error(
                        null,
                        ErrorType.UNKNOWN,
                    )
                }
            }
            .buffer().collect {
                res(it)
            }
    }
}
