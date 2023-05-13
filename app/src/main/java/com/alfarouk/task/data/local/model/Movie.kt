package com.alfarouk.task.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey
    val id: String,
    val title: String?,
    val backdrop_path: String?,
)