package com.alfarouk.task.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alfarouk.task.data.local.model.Movie
import com.alfarouk.task.domain.entities.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM Movie ")
    suspend fun getAllMovies(): List<Movie>

    @Query("DELETE FROM Movie")
    suspend fun deleteAllMovies()
}