package com.lelestacia.thelorrytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.lelestacia.thelorrytest.ui.screen.list.ListRestaurantScreen
import com.lelestacia.thelorrytest.ui.screen.list.ListRestaurantViewModel
import com.lelestacia.thelorrytest.ui.theme.TheLorryTestTheme
import com.lelestacia.thelorrytest.util.Resource
import com.lelestacia.thelorrytest.util.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.retry

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberAnimatedNavController()
            TheLorryTestTheme {
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
                            enterTransition = null,
                            exitTransition = null
                        ) {
                            val viewModel: ListRestaurantViewModel = hiltViewModel()
                            val selectedCategory by viewModel.selectedCategories.collectAsStateWithLifecycle()
                            val restaurants by viewModel.restaurants.collectAsStateWithLifecycle(
                                initialValue = Resource.None
                            )

                            ListRestaurantScreen(
                                restaurantResources = restaurants,
                                onRestaurantClicked = {},
                                selectedCategories = selectedCategory,
                                onCategoriesClicked = viewModel::onCategoriesChanged,
                                onRetry = {
                                    viewModel.restaurants.retry()
                                }
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
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                                    animationSpec = tween()
                                ) + fadeIn(tween())
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                                    animationSpec = tween()
                                ) + fadeOut(tween())
                            }
                        ) {
                            val restaurantID = it.arguments?.getInt("restaurant_id") ?: 0
                            Text(text = restaurantID.toString())
                        }
                    }
                }
            }
        }
    }
}