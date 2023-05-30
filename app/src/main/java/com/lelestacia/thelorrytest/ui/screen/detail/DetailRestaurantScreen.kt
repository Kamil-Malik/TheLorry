package com.lelestacia.thelorrytest.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lelestacia.thelorrytest.domain.model.DetailRestaurant
import com.lelestacia.thelorrytest.ui.theme.Gotham
import com.lelestacia.thelorrytest.ui.theme.TheLorryTestTheme
import com.lelestacia.thelorrytest.ui.theme.starColor
import com.lelestacia.thelorrytest.util.Resource
import com.lelestacia.thelorrytest.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRestaurantScreen(
    navController: NavHostController,
    detailRestaurant: Resource<DetailRestaurant>,
) {
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route
    Scaffold(
        topBar = {
            TopAppBar(title = {}, navigationIcon = {
                IconButton(onClick = {
                    if (currentRoute == Screen.ListRestaurant.route) {
                        return@IconButton
                    }
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Button",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (detailRestaurant) {
                is Resource.Error -> {}
                Resource.Loading -> {}
                Resource.None -> {}
                is Resource.Success -> {
                    Text(
                        text = detailRestaurant.data?.title as String,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(
                            items = detailRestaurant.data.images,
                            key = { foodImages ->
                                foodImages.url
                            }
                        ) { food ->
                            AsyncImage(
                                model = food.url,
                                contentDescription = "Food images",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Rating:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                        )
                        val ratingValue: Int = detailRestaurant.data.rating
                        val leftOver = 5 - ratingValue
                        Row {
                            for (i in 0..detailRestaurant.data.rating) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = starColor
                                )
                            }
                            if (leftOver != 0) {
                                Icon(
                                    imageVector = Icons.Filled.StarBorder,
                                    contentDescription = null,
                                    tint = starColor
                                )
                            }
                        }
                        Text(text = "$ratingValue/5")
                    }
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        val address = detailRestaurant.data.address.fullName
                        Text(
                            text = address,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { /*TODO*/ },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Copy Address"
                            )
                        }
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null)
                        Text(
                            text = "Navigate with Google Map"
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Description",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = detailRestaurant.data.description,
                            textAlign = TextAlign.Justify,
                            lineHeight = 19.sp,
                            fontFamily = Gotham,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDetailRestaurant() {
    val detailRestaurant = DetailRestaurant(
        title = "Tom's Kitchen",
        images = listOf(
            DetailRestaurant.ImageUrl(
                url = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food-composition_23-2149141352.jpg?w=1380&t=st=1685077948~exp=1685078548~hmac=dc6e59db72bc9f6dd6c9658f7e049882c1057ba1c3d1fde2f1311dec21681706"
            ),
            DetailRestaurant.ImageUrl(
                url = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food_23-2149141313.jpg?t=st=1685055169~exp=1685055769~hmac=f0546d7234bba701fee3392f7c16d57c322806cfc881e19100ddeb51977ca365"
            ),
            DetailRestaurant.ImageUrl(
                url = "https://img.freepik.com/premium-photo/traditional-spanish-breakfast-tostada-with-different-toppings-dark-background_79782-3251.jpg?w=1380"
            ),
            DetailRestaurant.ImageUrl(
                url = "https://img.freepik.com/free-photo/fruit-salad-spilling-floor-was-mess-vibrant-colors-textures-generative-ai_8829-2895.jpg?w=826&t=st=1685078122~exp=1685078722~hmac=6db09ebf5256817a8f3e8f2043f9ba9275fe8614bb954eb35070c7ba2267c2c5"
            ),
            DetailRestaurant.ImageUrl(
                url = "https://img.freepik.com/free-psd/delicous-asian-food-social-media-template_505751-2982.jpg?w=1380&t=st=1685080108~exp=1685080708~hmac=b10d601c91849abd3165ddf1fa919da2a5aa399bab5e889eb6ec6b40d08d921a"
            )
        ),
        rating = 4,
        address = DetailRestaurant.RestaurantAddress(
            fullName = "Jl. RC. Veteran Raya No.9, Bintaro, Kec. Pesanggrahan, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12330",
            lat = "-6.2830295",
            lng = "106.7940221"
        ),
        description = "The Stick is a delightful food place that specializes in grilled beef steaks. Located in a cozy setting, it offers a mouthwatering selection of perfectly cooked steaks served on a dark wooden surface. The aroma of sizzling meat fills the air, enticing diners with its irresistible appeal. Whether you prefer your steak rare, medium, or well-done, The Stick ensures a culinary experience that satisfies even the most discerning meat lovers. Indulge in their succulent steaks, accompanied by a range of delectable sides and sauces, for a truly memorable dining experience."
    )

    TheLorryTestTheme {
        Surface {
            DetailRestaurantScreen(
                navController = rememberNavController(),
                detailRestaurant = Resource.Success(detailRestaurant)
            )
        }
    }
}