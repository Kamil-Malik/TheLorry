package com.lelestacia.thelorrytest.ui.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.ui.component.CommentItem
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantAddressAndOpenMaps
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantDescription
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantDetailPostComment
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantRating
import com.lelestacia.thelorrytest.ui.screen.detail.component.RestaurantTitleAndShowcase
import com.lelestacia.thelorrytest.ui.screen.utility.ErrorScreen
import com.lelestacia.thelorrytest.ui.screen.utility.LoadingScreen
import com.lelestacia.thelorrytest.ui.theme.TheLorryTestTheme
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRestaurantScreen(
    navController: NavHostController,
    screenState: DetailRestaurantScreenState,
    onEvent: (DetailRestaurantScreenEvent) -> Unit,
    sendCommentStatus: Resource<String>
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val restaurantDetail = screenState.restaurantDetail
    val restaurantComments = screenState.restaurantDetailComments
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollState = rememberScrollState()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val unknownError = stringResource(id = R.string.unknown_error)
    LaunchedEffect(key1 = sendCommentStatus, block = {
        if (sendCommentStatus is Resource.Success) {
            snackBarHostState.showSnackbar(sendCommentStatus.data as String)
        } else if (sendCommentStatus is Resource.Error) {
            snackBarHostState.showSnackbar(sendCommentStatus.message ?: unknownError)
        }
    })
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = navController::popBackStack,
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back_button_assistive),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = restaurantComments.second is Resource.Success || restaurantComments.first.isNotEmpty(),
                enter = fadeIn() + slideInVertically()
            ) {
                RestaurantDetailPostComment(
                    userComment = screenState.userComment,
                    onUserCommentChanged = { newUserComment ->
                        onEvent(
                            DetailRestaurantScreenEvent.OnUserCommentChanged(
                                comment = newUserComment
                            )
                        )
                    },
                    onSendUserComment = {
                        onEvent(
                            DetailRestaurantScreenEvent.OnSendUserComment
                        )
                    },
                    sendCommentStatus = sendCommentStatus
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
        ) {
            when (restaurantDetail) {
                is Resource.Error -> {
                    ErrorScreen(
                        errorMessage = restaurantDetail.message ?: unknownError,
                        onRetry = {
                            onEvent(
                                DetailRestaurantScreenEvent.OnRetryRestaurantDetailRestaurant
                            )
                        },
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
                    RestaurantAddressAndOpenMaps(
                        address = address,
                        onErrorOpeningMaps = { errorMessage ->
                            scope.launch {
                                snackBarHostState.showSnackbar(errorMessage)
                            }
                        }
                    )

                    val description = detail.description
                    RestaurantDescription(description = description)
                }
            }

            val size by animateIntAsState(
                targetValue = restaurantComments.first.size,
                label = "comments"
            )
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
                        items(
                            items = restaurantComments.first,
                            key = { comment -> comment.id }
                        ) { comment ->
                            CommentItem(comment = comment)
                        }

                        when (restaurantComments.second) {
                            is Resource.Error -> {
                                item {
                                    ErrorScreen(
                                        errorMessage = restaurantComments.second.message
                                            ?: unknownError,
                                        onRetry = {
                                            onEvent(
                                                DetailRestaurantScreenEvent.OnRetryOrLoadNextComment
                                            )
                                        },
                                        modifier = Modifier.height(
                                            if (restaurantComments.first.isEmpty()) 500.dp
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
                                                if (restaurantComments.first.isEmpty()) 500.dp
                                                else 125.dp
                                            )
                                    )
                                }
                            }

                            is Resource.Success -> {
                                if (screenState.hasNextPage) {
                                    item {
                                        TextButton(
                                            onClick = {
                                                onEvent(
                                                    DetailRestaurantScreenEvent.OnRetryOrLoadNextComment
                                                )
                                            },
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
    TheLorryTestTheme {
        Surface {
            DetailRestaurantScreen(
                navController = rememberNavController(),
                screenState = DetailRestaurantScreenState(),
                onEvent = {},
                sendCommentStatus = Resource.None
            )
        }
    }
}