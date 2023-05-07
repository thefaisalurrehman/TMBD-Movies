package com.example.tmdbmovies.models.repositories

import android.util.Log
import com.example.tmdbmovies.common.AppConstants
import com.example.tmdbmovies.models.datamodels.MovieCredits
import com.example.tmdbmovies.models.datamodels.MovieDetail
import com.example.tmdbmovies.models.datamodels.TMDBMovies
import com.example.tmdbmovies.models.datasources.api.TMDBApi
import com.example.tmdbmovies.models.datasources.local.dao.MovieCastDao
import com.example.tmdbmovies.models.datasources.local.dao.MovieDao
import com.example.tmdbmovies.models.datasources.local.dao.MovieDetailDao
import com.example.tmdbmovies.models.datasources.utils.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StartUpRepository @Inject constructor(
    private var movieDao: MovieDao,
    private var tmdbApi: TMDBApi,
    private var movieDetailDao: MovieDetailDao,
    private var movieCastDao: MovieCastDao
) {
    companion object {
        private const val TAG = "StartUpRepository"
    }

    fun getUpComingMovies(): Flow<Resource<List<TMDBMovies.Results>>> {
        return flow {
            try {
                emit(Resource.success(movieDao.getTMDBMovies("upcoming")))
                tmdbApi.getUpComingMovies(AppConstants.API_KEY).let {
                    if (it.isSuccessful) {
                        Log.d(TAG, "getTrendingMoviesList: ${it.body()!!.results}")
                        it.body()!!.results.forEach { movie->
                            movie.movieType = "upcoming"
                            movieDao.insertMovies(movie)
                        }
                        emit(Resource.success(movieDao.getTMDBMovies("upcoming")))
                    } else {
                        emit(Resource.error(it.body()!!.status_message!!, data = null))
                    }
                }
            }
            catch (ex : Exception){
                Log.d(TAG, "getMovieDetails: ${ex.message}")
                emit(Resource.error("something went wrong", data = null))
            }

        }.flowOn(IO)
    }

    fun getTopRatedMovies(): Flow<Resource<List<TMDBMovies.Results>>> {
        return flow {
            try {
                emit(Resource.success(movieDao.getTMDBMovies("topRated")))
                tmdbApi.getTopRatedMovies(AppConstants.API_KEY).let {
                    if (it.isSuccessful) {
                        Log.d(TAG, "getTrendingMoviesList: ${it.body()!!.results}")
                        it.body()!!.results.forEach { movie->
                            movie.movieType = "topRated"
                            movieDao.insertMovies(movie)
                        }
                        emit(Resource.success(movieDao.getTMDBMovies("topRated")))
                    } else {
                        emit(Resource.error(it.body()!!.status_message!!, data = null))
                    }
                }
            }
            catch (ex : Exception){
                Log.d(TAG, "getMovieDetails: ${ex.message}")
                emit(Resource.error("something went wrong", data = null))
            }

        }.flowOn(IO)
    }

    fun getTrendingMovies(): Flow<Resource<List<TMDBMovies.Results>>> {
        return flow {
            try {
                emit(Resource.success(movieDao.getTMDBMovies("trending")))
                tmdbApi.getTrendingMovies(AppConstants.API_KEY).let {
                    if (it.isSuccessful) {
                        Log.d(TAG, "getTrendingMoviesList: ${it.body()!!.results}")
                        it.body()!!.results.forEach { movie->
                            movie.movieType = "trending"
                            movieDao.insertMovies(movie)
                        }
                        emit(Resource.success(movieDao.getTMDBMovies("trending")))
                    } else {
                        emit(Resource.error(it.body()!!.status_message!!, data = null))
                    }
                }
            }
            catch (ex : Exception){
                Log.d(TAG, "getMovieDetails: ${ex.message}")
                emit(Resource.error("something went wrong", data = null))
            }

        }.flowOn(IO)
    }

    fun getMovieDetails(movieId: Int, movieLanguage: String): Flow<Resource<MovieDetail>> {
        return flow {
            try {
                emit(Resource.success(movieDetailDao.getTMDBMovieById(movieId)))
                tmdbApi.getMovieDetails(movieId,AppConstants.API_KEY, movieLanguage,"videos").let {
                    if (it.isSuccessful) {
                        Log.d(TAG, "getMovieDetails: ${it.body()!!}")
                        movieDetailDao.insertMovie(it.body()!!)
                        emit(Resource.success(movieDetailDao.getTMDBMovieById(movieId)))
                    } else {
                        emit(Resource.error("something went wrong", data = null))
                    }
                }
            }catch (ex : Exception){
                Log.d(TAG, "getMovieDetails: ${ex.message}")
                emit(Resource.error("something went wrong", data = null))
            }

        }.flowOn(IO)
    }


    fun getMovieCredits(movieId: Int, movieLanguage: String): Flow<Resource<List<MovieCredits.MovieCast>>> {
        return flow {
            try {
                emit(Resource.success(movieCastDao.getMovieCast(movieId)))
                tmdbApi.getMovieCredits(movieId,AppConstants.API_KEY, movieLanguage).let {
                    if (it.isSuccessful) {
                        Log.d(TAG, "getMovieDetails: ${it.body()!!}")
                        it.body()!!.cast.forEach { movieCast ->
                            movieCast.movieId = movieId
                            movieCastDao.insertMovieCast(movieCast)
                        }
                        emit(Resource.success(movieCastDao.getMovieCast(movieId)))
                    } else {
                        emit(Resource.error("something went wrong", data = null))
                    }
                }
            }catch (ex : Exception){
                Log.d(TAG, "getMovieDetails: ${ex.message}")
                emit(Resource.error("something went wrong", data = null))
            }

        }.flowOn(IO)
    }
}