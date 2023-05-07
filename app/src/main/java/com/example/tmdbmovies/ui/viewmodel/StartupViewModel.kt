package com.example.tmdbmovies.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbmovies.models.datamodels.MovieCredits
import com.example.tmdbmovies.models.datamodels.MovieDetail
import com.example.tmdbmovies.models.datamodels.TMDBMovies
import com.example.tmdbmovies.models.datasources.utils.Resource
import com.example.tmdbmovies.models.repositories.StartUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(private var startUpRepository: StartUpRepository) :
    ViewModel() {


     var _trending: MutableStateFlow<Resource<List<TMDBMovies.Results>>> = MutableStateFlow(
        Resource.success(
            emptyList()
        )
    )
     var _topRated: MutableStateFlow<Resource<List<TMDBMovies.Results>>> = MutableStateFlow(
        Resource.success(
            emptyList()
        )
    )
     var _upComing: MutableStateFlow<Resource<List<TMDBMovies.Results>>> = MutableStateFlow(
        Resource.success(
            emptyList()
        )
    )

    val isMovieDetailsLoaded = mutableStateOf(false)

      var _movieDetails: MutableStateFlow<Resource<MovieDetail?>>
     = MutableStateFlow(Resource.success(null))
    var _movieCredits: MutableStateFlow<Resource<List<MovieCredits.MovieCast>>>
     =MutableStateFlow(Resource.success(emptyList()))


    init {
        getTrendingMovies()
        getTopRatedMovies()
        getUpComingMovies()
    }

    private fun getTrendingMovies() = viewModelScope.launch {
        val trending = startUpRepository.getTrendingMovies()
        trending.collect {
            _trending.value = it
        }
    }


    private fun getTopRatedMovies() = viewModelScope.launch {
        startUpRepository.getTopRatedMovies().collect {
            _topRated.value = it
        }
    }

    private fun getUpComingMovies() = viewModelScope.launch {
        startUpRepository.getUpComingMovies().collect {
            _upComing.value = it
        }
    }


    private fun getMovieCredits(
        movieId: Int
    ) = viewModelScope.launch {
        startUpRepository.getMovieCredits(movieId, "en")
            .collect { _movieCredits.value = it }
    }

    fun getMovieDetails(movieId: Int) = viewModelScope.launch {
        startUpRepository.getMovieDetails(movieId, "en").collect {
            _movieDetails.value = it
        if (it.data!=null){
            isMovieDetailsLoaded.value = true
        }
        }
        getMovieCredits(movieId)
    }

}