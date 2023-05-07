package com.example.tmdbmovies.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tmdbmovies.ui.screens.HomeScreen
import com.example.tmdbmovies.ui.screens.MovieDetailScreen
import com.example.tmdbmovies.ui.viewmodel.StartupViewModel

@Composable
fun SetupNavGraph(navController: NavHostController, startupViewModel: StartupViewModel) {

    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(route = Screen.Home.route){
            HomeScreen(startupViewModel){
                startupViewModel.getMovieDetails(it)
                navController.navigate(Screen.Details.route)
            }
        }
        composable(route = Screen.Details.route){
            MovieDetailScreen(startupViewModel){
                navController.popBackStack()
            }
        }
    }
}