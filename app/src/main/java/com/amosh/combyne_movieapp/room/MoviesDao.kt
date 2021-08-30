package com.amosh.combyne_movieapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amosh.combyne_movieapp.util.Constants.MOVIE_TABLE_NAME

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieCacheEntity): Long

    @Query("SELECT * FROM $MOVIE_TABLE_NAME")
    suspend fun get(): List<MovieCacheEntity>
}