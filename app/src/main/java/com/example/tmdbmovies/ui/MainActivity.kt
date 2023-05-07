package com.example.tmdbmovies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tmdbmovies.ui.nav.SetupNavGraph
import com.example.tmdbmovies.ui.theme.TMDBMoviesTheme
import com.example.tmdbmovies.ui.viewmodel.StartupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TMDBMoviesTheme {
                val startupViewModel = viewModel<StartupViewModel>()
                navController = rememberNavController()
                SetupNavGraph(navController, startupViewModel)
            }

        }


    }


}