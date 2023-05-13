package com.alfarouk.task.domain.entities

import java.io.Serializable

data class MovieEntity(
    val title: String,
    val imageUrl: String,
) : Serializable