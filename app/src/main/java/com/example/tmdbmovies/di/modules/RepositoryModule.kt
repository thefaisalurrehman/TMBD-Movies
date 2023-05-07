package com.example.tmdbmovies.di.modules

import com.example.tmdbmovies.models.datasources.api.TMDBApi
import com.example.tmdbmovies.models.datasources.local.dao.MovieCastDao
import com.example.tmdbmovies.models.datasources.local.dao.MovieDao
import com.example.tmdbmovies.models.datasources.local.dao.MovieDetailDao
import com.example.tmdbmovies.models.repositories.StartUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideStartUpRepository(movieDao: MovieDao,tmdbApi: TMDBApi,movieDetailDao: MovieDetailDao,movieCastDao: MovieCastDao) : StartUpRepository{
        return StartUpRepository(movieDao = movieDao,tmdbApi = tmdbApi, movieDetailDao = movieDetailDao,movieCastDao)
    }
}