package com.alfarouk.task.domain.usecase.getOfflineMoviesUseCase

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


class GetOfflineMoviesUseCase @Inject constructor(private val repository: Repository) {
    suspend fun load(): Resource<List<MovieEntity>> {
        lateinit var result: Resource<List<MovieEntity>>
        result { result = it }
        return result
    }

    private suspend fun result(
        res: (Resource<List<MovieEntity>>) -> Unit
    ) {
        flow {
            emit(repository.getOfflineMovies())
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