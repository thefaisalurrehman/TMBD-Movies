package com.example.tmdbmovies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tmdbmovies.R
import com.example.tmdbmovies.ui.theme.Black_custom

@Composable
fun MovieListItems(
    title: String, path: String, ratingText: String, id: Int, onClick: (id: Int) -> Unit
) {

    Box(modifier = Modifier
        .padding(4.dp)
        .width(100.dp)
        .height(200.dp)
        .background(Black_custom)
        .clickable {
            onClick(id)
        }) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .width(100.dp)
                    .height(150.dp)
                    .background(Black_custom)
                    .clip(shape = RoundedCornerShape(16))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(path).crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.panda_3),
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp),
                    contentScale = ContentScale.Crop,
                )

            }
            Text(text = title, color = Color.White, maxLines = 1, fontSize = 12.sp)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.star_fill),
                    contentDescription = null,
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    text = ratingText, color = Color.White, maxLines = 1, fontSize = 12.sp
                )
            }
        }


    }


}