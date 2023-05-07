package com.example.tmdbmovies.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tmdbmovies.R
import com.example.tmdbmovies.common.AppConstants
import com.example.tmdbmovies.models.datamodels.MovieCredits
import com.example.tmdbmovies.models.datamodels.MovieDetail
import com.example.tmdbmovies.models.datamodels.TMDBMovies
import com.example.tmdbmovies.models.datasources.utils.Resource
import com.example.tmdbmovies.models.datasources.utils.Status
import com.example.tmdbmovies.ui.theme.Black_custom
import com.example.tmdbmovies.ui.theme.Divider_Color
import com.example.tmdbmovies.ui.theme.Gray_custom
import com.example.tmdbmovies.ui.theme.Primary_Color
import com.example.tmdbmovies.ui.viewmodel.StartupViewModel


@Composable
fun MovieDetailScreen(
    startupViewModel: StartupViewModel, backPress: () -> Unit
) {

    if (startupViewModel.isMovieDetailsLoaded.value) {
        val movieDetails = startupViewModel._movieDetails.collectAsState()
        val movieCredits = startupViewModel._movieCredits.collectAsState()
        if (movieDetails.value.data != null) {
            DetailsScreen(movieDetails.value, movieCredits.value) {
                backPress()
            }
        } else {
            SomethingWrong()
        }
    }


}

@Composable
fun SomethingWrong() {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = "Something went wrong",
            color = Color.Black,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(
                Center
            )
        )
    }
}

@Composable
private fun DetailsScreen(
    movieDetail: Resource<MovieDetail?>,
    movieCredits: Resource<List<MovieCredits.MovieCast>>,
    backPress: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Black_custom)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            val path = AppConstants.LOAD_BACK_DROP_BASE_URL + movieDetail.data?.backdrop_path
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(path).crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.panda_3),
                contentDescription = "Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
            Image(
                rememberAsyncImagePainter(R.drawable.main_background),
                contentDescription = "Foreground",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .shadow(elevation = 1.dp)
            )


            Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_arrow_back_ios_new_24),
                contentDescription = "Back Press",
                modifier = Modifier
                    .padding(start = 8.dp, top = 12.dp)
                    .size(24.dp)
                    .clickable {
                        backPress()
                    })

        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(top = 150.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = movieDetail.data?.title.toString(),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Genres(genres = movieDetail.data?.genres ?: emptyList())
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Divider_Color)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_schedule_24),
                    contentDescription = null,
                    Modifier
                        .size(24.dp)
                        .weight(1f),
                    tint = Primary_Color
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_star_rate_24),
                    contentDescription = null,
                    Modifier
                        .size(24.dp)
                        .weight(1f),
                    tint = Primary_Color
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_thumb_up_24),
                    contentDescription = null,
                    Modifier
                        .size(24.dp)
                        .weight(1f),
                    tint = Primary_Color
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                val hours: Int? =
                    movieDetail.data?.runtime?.div(60) //since both are ints, you get an int

                val minutes: Int = movieDetail.data?.runtime?.rem(60) ?: 0
                Text(
                    text = "${hours}h ${minutes}m",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 11.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${movieDetail.data?.vote_average}/10",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 11.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${movieDetail.data?.vote_count}",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 11.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Divider_Color)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            )

            Text(
                text = "Storyline",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp, top = 12.dp)
            )
            Text(
                text = movieDetail.data?.overview ?: "",
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp, top = 12.dp)
            )
            Text(
                text = "Full Cast & Crew",
                fontSize = 17.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp, top = 12.dp)
            )
            CastList(cast = movieCredits.data)
        }

    }

}

@Composable
fun Genres(genres: List<MovieDetail.Genres>) {
    LazyRow(content = {
        items(genres) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .widthIn(min = 60.dp, max = 120.dp)
                    .heightIn(min = 20.dp)
                    .background(
                        Color.Transparent
                    ),
                border = BorderStroke(0.5.dp, Gray_custom),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                ),
            ) {
                Text(
                    text = it.name ?: "",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Thin,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxHeight()
                        .padding(horizontal = 6.dp)
                )
            }
        }
    })
}

@Composable
private fun CastList(cast: List<MovieCredits.MovieCast>?) {
    LazyRow(content = {
        cast?.let {
            items(it) { item ->
                val path = AppConstants.LOAD_IMAGE_BASE_URL + item.profile_path
                MovieListItems(
                    item.name.toString(), path
                )
            }
        }

    })
}


@Composable
private fun MovieListItems(
    title: String, path: String
) {

    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(90.dp)
            .height(150.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .width(90.dp)
                    .height(110.dp)
                    .clip(shape = RoundedCornerShape(16))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(path).crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.panda_3),
                    contentDescription = null,
                    modifier = Modifier
                        .width(90.dp)
                        .height(110.dp),
                    contentScale = ContentScale.Crop,
                )
            }

            Text(
                text = title,
                color = Color.White,
                maxLines = 1,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )

        }


    }


}

@Composable
private fun Result(
    _result: Resource<List<TMDBMovies.Results>>,
    result: @Composable (List<TMDBMovies.Results>?) -> Unit
) {
    _result.let {
        when (_result.status) {
            Status.LOADING -> {

            }

            Status.SUCCESS -> {
                if (!it.data.isNullOrEmpty()) {
                    result(it.data)
                } else {
                    //
                }
            }

            Status.ERROR -> {
                //
            }
        }
    }
}