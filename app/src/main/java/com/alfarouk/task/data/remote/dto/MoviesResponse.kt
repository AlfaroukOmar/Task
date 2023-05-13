package com.alfarouk.task.data.remote.dto

import com.alfarouk.task.data.local.model.Movie
import com.google.gson.annotations.SerializedName

data class MoviesResponse(

    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("total_pages")
    val totalPages: Int? = null,

    @field:SerializedName("results")
    val results: List<Movie>? = null,

    @field:SerializedName("total_results")
    val totalResults: Int? = null
)

