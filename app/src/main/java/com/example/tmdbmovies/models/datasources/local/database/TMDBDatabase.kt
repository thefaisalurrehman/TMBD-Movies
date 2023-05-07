package com.example.tmdbmovies.models.datasources.local.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.example.tmdbmovies.common.AppConstants.APP_DATABASE
import com.example.tmdbmovies.models.datamodels.MovieCredits
import com.example.tmdbmovies.models.datamodels.MovieDetail
import com.example.tmdbmovies.models.datamodels.TMDBMovies
import com.example.tmdbmovies.models.datasources.local.dao.MovieCastDao
import com.example.tmdbmovies.models.datasources.local.dao.MovieDao
import com.example.tmdbmovies.models.datasources.local.dao.MovieDetailDao
import com.example.tmdbmovies.models.datasources.utils.converters.GenresTypeConverter

@Database(
    entities = [TMDBMovies.Results::class,MovieDetail::class,MovieCredits.MovieCast::class], version = 1, exportSchema = true
)
@TypeConverters(
    GenresTypeConverter::class)
abstract class TMDBDatabase : RoomDatabase() {

    abstract fun moviesDao(): MovieDao
    abstract fun movieDetailDao() : MovieDetailDao
    abstract fun movieCastDao() : MovieCastDao

    companion object {
        val DATABASE_NAME = APP_DATABASE
    }
}