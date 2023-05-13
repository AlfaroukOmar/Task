package com.alfarouk.task.data.remote

import com.alfarouk.task.data.remote.dto.MoviesResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface MovieApi {
    @POST("popular")
    suspend fun getMovies(
        @Query("page") page: Int,
    ): Response<MoviesResponse>
}