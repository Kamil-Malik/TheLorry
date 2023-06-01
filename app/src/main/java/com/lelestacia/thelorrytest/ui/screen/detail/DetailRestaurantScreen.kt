package com.lelestacia.thelorrytest.ui.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.PagingData
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.domain.model.Comment
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.ui.component.CommentItem
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantAddressAndOpenMaps
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantDescription
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantRating
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantTitleAndShowcase
import com.lelestacia.thelorrytest.ui.screen.utility.ErrorScreen
import com.lelestacia.thelorrytest.ui.screen.utility.LoadingScreen
import com.lelestacia.thelorrytest.util.Resource
import com.lelestacia.thelorrytest.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRestaurantScreen(
    navController: NavHostController,
    restaurantDetail: Resource<RestaurantDetail>,
    comments: SnapshotStateList<Comment>,
    commentsLoadState: Resource<Any>,
    hasNextPage: Boolean,
    onNextComment: () -> Unit
) {
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (currentRoute == Screen.ListRestaurant.route) {
                                return@IconButton
                            }
                            navController.popBackStack()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    )
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (restaurantDetail) {
                is Resource.Error -> ErrorScreen(errorMessage = "", onRetry = { })

                Resource.Loading -> LoadingScreen()

                Resource.None -> Unit
                is Resource.Success -> {
                    val detail: RestaurantDetail = restaurantDetail.data as RestaurantDetail

                    RestaurantTitleAndShowcase(
                        title = detail.title,
                        images = detail.images
                    )

                    val rating = detail.rating
                    RestaurantRating(rating = rating)

                    val address = detail.address
                    RestaurantAddressAndOpenMaps(address = address)

                    val description = detail.description
                    RestaurantDescription(description = description)
                }
            }

            AnimatedVisibility(
                visible = comments.size != 0,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Text(
                    text = stringResource(R.string.comment_count, comments.size),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .height(500.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(comments) { comment ->
                    CommentItem(comment = comment)
                }

                when (commentsLoadState) {
                    is Resource.Error -> {

                    }

                    Resource.Loading -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(
                                        if (comments.isEmpty()) 500.dp
                                        else 125.dp
                                    )
                            ) {
                                LinearProgressIndicator()
                            }
                        }
                    }

                    else -> Unit
                }

                item {
                    AnimatedVisibility(visible = hasNextPage && commentsLoadState is Resource.Success) {
                        Button(onClick = onNextComment::invoke) {
                            Text(text = "Load next page")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDetailRestaurant() {
    val restaurantDetail = RestaurantDetail(
        title = "Tom's Kitchen",
        images = listOf(
            RestaurantDetail.ImageUrl(
                url = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food-composition_23-2149141352.jpg?w=1380&t=st=1685077948~exp=1685078548~hmac=dc6e59db72bc9f6dd6c9658f7e049882c1057ba1c3d1fde2f1311dec21681706"
            ),
            RestaurantDetail.ImageUrl(
                url = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food_23-2149141313.jpg?t=st=1685055169~exp=1685055769~hmac=f0546d7234bba701fee3392f7c16d57c322806cfc881e19100ddeb51977ca365"
            ),
            RestaurantDetail.ImageUrl(
                url = "https://img.freepik.com/premium-photo/traditional-spanish-breakfast-tostada-with-different-toppings-dark-background_79782-3251.jpg?w=1380"
            ),
            RestaurantDetail.ImageUrl(
                url = "https://img.freepik.com/free-photo/fruit-salad-spilling-floor-was-mess-vibrant-colors-textures-generative-ai_8829-2895.jpg?w=826&t=st=1685078122~exp=1685078722~hmac=6db09ebf5256817a8f3e8f2043f9ba9275fe8614bb954eb35070c7ba2267c2c5"
            ),
            RestaurantDetail.ImageUrl(
                url = "https://img.freepik.com/free-psd/delicous-asian-food-social-media-template_505751-2982.jpg?w=1380&t=st=1685080108~exp=1685080708~hmac=b10d601c91849abd3165ddf1fa919da2a5aa399bab5e889eb6ec6b40d08d921a"
            )
        ),
        rating = 4,
        address = RestaurantDetail.RestaurantAddress(
            fullName = "Jl. RC. Veteran Raya No.9, Bintaro, Kec. Pesanggrahan, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12330",
            lat = "-6.2830295",
            lng = "106.7940221"
        ),
        description = "The Stick is a delightful food place that specializes in grilled beef steaks. Located in a cozy setting, it offers a mouthwatering selection of perfectly cooked steaks served on a dark wooden surface. The aroma of sizzling meat fills the air, enticing diners with its irresistible appeal. Whether you prefer your steak rare, medium, or well-done, The Stick ensures a culinary experience that satisfies even the most discerning meat lovers. Indulge in their succulent steaks, accompanied by a range of delectable sides and sauces, for a truly memorable dining experience."
    )

    val comments = listOf(
        Comment(
            id = 5210,
            userName = "Tom John",
            body = "Best food in town! Highly recommended!",
            profilePicture = "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=1380&t=st=1685080702exp=1685081302hmac=25064c0fcde5709896af5d58a8c55d5da65692c5f30292965d9f914bdaa34959"
        )
    )

    val pagingDataComment = PagingData.from(comments)


}