package com.amosh.movieapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amosh.movieapp.utils.Constants.MOVIE_TABLE_NAME

@Entity(tableName = MOVIE_TABLE_NAME)
data class MovieCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "objectId")
    val objectId: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: String,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "releaseDate")
    val releaseDate: String,

    @ColumnInfo(name = "seasons")
    val seasons: Double?
)
