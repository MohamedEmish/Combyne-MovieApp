package com.amosh.movieapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieCacheEntity::class], version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {

    }
}