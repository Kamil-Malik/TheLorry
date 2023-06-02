package com.lelestacia.thelorrytest.ui.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import androidx.navigation.compose.rememberNavController
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.domain.model.Comment
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.domain.model.TomKitchen
import com.lelestacia.thelorrytest.ui.component.CommentItem
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantAddressAndOpenMaps
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantDescription
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantRating
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantTitleAndShowcase
import com.lelestacia.thelorrytest.ui.screen.utility.ErrorScreen
import com.lelestacia.thelorrytest.ui.screen.utility.LoadingScreen
import com.lelestacia.thelorrytest.ui.theme.TheLorryTestTheme
import com.lelestacia.thelorrytest.util.Resource
import com.lelestacia.thelorrytest.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRestaurantScreen(
    navController: NavHostController,
    restaurantDetail: Resource<RestaurantDetail>,
    onRetry: () -> Unit,
    comments: SnapshotStateList<Comment>,
    commentsLoadState: Resource<Any>,
    hasNextPage: Boolean,
    onRetryOrNextComment: () -> Unit,
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
            AnimatedVisibility(
                visible = commentsLoadState is Resource.Success || comments.isNotEmpty(),
                enter = fadeIn() + slideInVertically()
            ) {
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
                        TextField(
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
        ) {
            when (restaurantDetail) {
                is Resource.Error -> {
                    ErrorScreen(
                        errorMessage = restaurantDetail.message
                            ?: stringResource(id = R.string.unknown_error),
                        onRetry = onRetry::invoke,
                    )
                }

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

            val size by animateIntAsState(targetValue = comments.size, label = "comments")
            AnimatedVisibility(visible = restaurantDetail is Resource.Success) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.comment_count, size),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    LazyColumn(
                        state = rememberLazyListState(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .height(500.dp),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 12.dp
                        )
                    ) {
                        items(comments) { comment ->
                            CommentItem(comment = comment)
                        }

                        when (commentsLoadState) {
                            is Resource.Error -> {
                                item {
                                    ErrorScreen(
                                        errorMessage = commentsLoadState.message
                                            ?: stringResource(id = R.string.unknown_error),
                                        onRetry = onRetryOrNextComment::invoke,
                                        modifier = Modifier.height(
                                            if (comments.isEmpty()) 500.dp
                                            else 250.dp
                                        )
                                    )
                                }
                            }

                            Resource.Loading -> {
                                item {
                                    LoadingScreen(
                                        modifier = Modifier
                                            .height(
                                                if (comments.isEmpty()) 500.dp
                                                else 125.dp
                                            )
                                    )
                                }
                            }

                            is Resource.Success -> {
                                if (hasNextPage) {
                                    item {
                                        TextButton(
                                            onClick = onRetryOrNextComment::invoke,
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = MaterialTheme.colorScheme.primary
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = stringResource(R.string.load_more_comments),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 16.sp
                                                )
                                            )
                                            Icon(
                                                imageVector = Icons.Default.KeyboardDoubleArrowDown,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }

                            else -> Unit
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
    val comment = remember {
        mutableStateListOf<Comment>()
    }

    TheLorryTestTheme {
        Surface {
            DetailRestaurantScreen(
                navController = rememberNavController(),
                restaurantDetail = Resource.Success(data = TomKitchen),
                onRetry = {},
                comments = comment,
                commentsLoadState = Resource.None,
                hasNextPage = false,
                onRetryOrNextComment = {})
        }
    }
}