package com.example.tmdbmovies.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tmdbmovies.R
import com.example.tmdbmovies.common.AppConstants
import com.example.tmdbmovies.models.datamodels.TMDBMovies
import com.example.tmdbmovies.models.datasources.utils.Resource
import com.example.tmdbmovies.models.datasources.utils.Status
import com.example.tmdbmovies.ui.theme.Black_custom
import com.example.tmdbmovies.ui.theme.Primary_Color
import com.example.tmdbmovies.ui.viewmodel.StartupViewModel


@Composable
fun HomeScreen(startupViewModel : StartupViewModel,
               onClick: (id: Int) -> Unit
) {

    val trending = startupViewModel._trending.collectAsState()
    val topRated = startupViewModel._topRated.collectAsState()
    val upComing = startupViewModel._upComing.collectAsState()


    val path = rememberSaveable { mutableStateOf("") }



    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Black_custom), contentAlignment = Alignment.Center,) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Box(contentAlignment = Alignment.TopCenter) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(path.value).crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.panda_3),
                    contentDescription = "Dummy Panda",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                )

                Image(
                    rememberAsyncImagePainter(R.drawable.main_background),
                    contentDescription = "Foreground",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .height(450.dp)
                        .fillMaxWidth()
                        .shadow(elevation = 2.dp),
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Result(trending.value){ results ->
                path.value = AppConstants.LOAD_BACK_DROP_BASE_URL + trending.value.data?.first()?.poster_path
                TrendingList(trending = results){onClick(it)}
            }
            Result(topRated.value){results ->
                TopRatedList(topRated = results){onClick(it)}
            }
            Result(upComing.value){results ->
                UpComingList(upComing = results){onClick(it)}
            }
        }
    }

}

@Composable
private fun UpComingList(upComing: List<TMDBMovies.Results>?, onClick: (id: Int) -> Unit) {
    StartDrawableText("Up Coming")
    LazyRow(content = {
        upComing?.let {
            items(it) { item ->
                val path = AppConstants.LOAD_IMAGE_BASE_URL + item.poster_path
                MovieListItems(
                    item.title.toString(),
                    path,
                    item.vote_average.toString(),
                    item.id ?: 0
                ) { id ->
                    onClick(id)
                }
            }
        }

    })
}

@Composable
private fun TopRatedList(topRated: List<TMDBMovies.Results>?, onClick: (id: Int) -> Unit) {
    StartDrawableText("Top Rated")
    LazyRow(content = {
        topRated?.let {
            items(it) { item ->
                val path = AppConstants.LOAD_IMAGE_BASE_URL + item.poster_path
                MovieListItems(
                    item.title.toString(),
                    path,
                    item.vote_average.toString(),
                    item.id ?: 0
                ) { id ->
                    onClick(id)
                }
            }
        }

    })
}

@Composable
private fun TrendingList(trending: List<TMDBMovies.Results>?, onClick: (id: Int) -> Unit) {
    StartDrawableText("Trending Now")
    LazyRow(content = {
        trending?.let {
            items(it) { item ->
                val path = AppConstants.LOAD_IMAGE_BASE_URL + item.poster_path
                MovieListItems(
                    item.title.toString(),
                    path,
                    item.vote_average.toString(),
                    item.id ?: 0
                ) { id ->
                    onClick(id)
                }
            }
        }

    })
}


@Composable
private fun StartDrawableText(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = ColorPainter(Primary_Color),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp)
                .width(4.dp)
                .height(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(start = 4.dp, top = 12.dp)
        )
    }

}
private const val TAG = "HomeScreen"
@Composable
private fun Result(
    _result: Resource<List<TMDBMovies.Results>>,
     result:@Composable (List<TMDBMovies.Results>?)->Unit
) {
    _result.let {
        when (_result.status) {
            Status.LOADING -> {
                Log.d(TAG, "Loading ....: ")
            }

            Status.SUCCESS -> {
                if (!it.data.isNullOrEmpty()) {
                    result(it.data)
                } else {
                    //
                }
            }

            Status.ERROR -> {
                Log.d(TAG, "Error: ${it.message}")
            }
        }
    }
}


