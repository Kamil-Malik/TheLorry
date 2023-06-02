package com.lelestacia.thelorrytest.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.ui.theme.TheLorryTestTheme
import kotlinx.coroutines.Dispatchers

@Composable
fun RestaurantItem(
    restaurant: Restaurant,
    onClicked: (Int) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                onClicked(restaurant.id)
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .fetcherDispatcher(Dispatchers.IO)
                .data(restaurant.image)
                .error(R.drawable.img)
                .build(),
            contentDescription = restaurant.title,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            colorFilter = ColorFilter.tint(
                color = Color.Black.copy(
                    alpha = 0.5F
                ),
                blendMode = BlendMode.Darken
            ),
            modifier = Modifier
                .aspectRatio(4F)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .matchParentSize()
                .padding(start = 16.dp)
        ) {
            Text(
                text = restaurant.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                ),
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    onClicked(restaurant.id)
                },
                content = {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = Color.White
                    )
                })
        }
    }
}

@Preview
@Composable
fun PreviewRestaurantItem() {
    val restaurant = Restaurant(
        id = 1,
        title = "Tom's Kitchen",
        image = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food-composition_23-2149141352.jpg?w=1380&t=st=1685077948~exp=1685078548~hmac=dc6e59db72bc9f6dd6c9658f7e049882c1057ba1c3d1fde2f1311dec21681706"
    )
    TheLorryTestTheme {
        Surface {
            RestaurantItem(
                restaurant = restaurant,
                onClicked = {}
            )
        }
    }
}