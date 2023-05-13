package com.alfarouk.task.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alfarouk.task.data.local.MovieDao
import com.alfarouk.task.data.local.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MovieDao(): MovieDao
}