package com.lelestacia.thelorrytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lelestacia.thelorrytest.ui.screen.detail.DetailRestaurantScreen
import com.lelestacia.thelorrytest.ui.screen.detail.DetailRestaurantViewModel
import com.lelestacia.thelorrytest.ui.screen.list.ListRestaurantScreen
import com.lelestacia.thelorrytest.ui.screen.list.ListRestaurantViewModel
import com.lelestacia.thelorrytest.ui.theme.TheLorryTestTheme
import com.lelestacia.thelorrytest.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberAnimatedNavController()
            val uiController = rememberSystemUiController()
            TheLorryTestTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Screen.ListRestaurant.route
                    ) {
                        composable(
                            route = Screen.ListRestaurant.route,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween()
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween()
                                )
                            }
                        ) {
                            uiController.setStatusBarColor(
                                color = MaterialTheme.colorScheme.background,
                                darkIcons = !isSystemInDarkTheme()
                            )

                            val viewModel: ListRestaurantViewModel = hiltViewModel()
                            val screenState by viewModel.listRestaurantScreenState.collectAsStateWithLifecycle()

                            ListRestaurantScreen(
                                navController = navController,
                                screenState = screenState,
                                onEvent = viewModel::onEvent
                            )
                        }

                        composable(
                            route = Screen.DetailRestaurant.route,
                            arguments = listOf(
                                navArgument(name = "restaurant_id") {
                                    type = NavType.IntType
                                }
                            ),
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween()
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween()
                                )
                            }
                        ) { navBackStackEntry ->
                            val restaurantID =
                                navBackStackEntry.arguments?.getInt("restaurant_id") ?: 0
                            val viewModel: DetailRestaurantViewModel = hiltViewModel()

                            LaunchedEffect(key1 = Unit) {
                                viewModel.updateRestaurantID(restaurantID)
                            }

                            val screenState by viewModel.detailRestaurantScreenState.collectAsStateWithLifecycle()
                            val sendCommentStatus by viewModel.sendCommentStatus.collectAsStateWithLifecycle()

                            DetailRestaurantScreen(
                                navController = navController,
                                screenState = screenState,
                                onEvent = viewModel::onEvent,
                                sendCommentStatus = sendCommentStatus
                            )
                        }
                    }
                }
            }
        }
    }
}