package com.alfarouk.task.data.Mapper

import com.alfarouk.task.data.local.model.Movie
import com.alfarouk.task.domain.entities.MovieEntity


fun Movie.ToEntity(): MovieEntity {
    return MovieEntity(
        title = this.title ?: " ",
        imageUrl = this.backdrop_path ?: " "
    )
}

