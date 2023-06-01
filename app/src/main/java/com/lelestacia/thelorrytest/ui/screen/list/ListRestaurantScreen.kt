package com.lelestacia.thelorrytest.ui.screen.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.ui.component.RestaurantItem
import com.lelestacia.thelorrytest.ui.screen.utility.ErrorScreen
import com.lelestacia.thelorrytest.ui.screen.utility.LoadingScreen
import com.lelestacia.thelorrytest.ui.theme.TheLorryTestTheme
import com.lelestacia.thelorrytest.util.Categories
import com.lelestacia.thelorrytest.util.Resource
import com.lelestacia.thelorrytest.util.Screen
import com.lelestacia.thelorrytest.util.getCategoriesAsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListRestaurantScreen(
    navController: NavHostController,
    restaurantResources: Resource<List<Restaurant>>,
    selectedCategories: Categories,
    onCategoriesClicked: (Categories) -> Unit,
    onRetry: () -> Unit
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
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Restaurant",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        width = 0.dp,
                        color = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FilterAlt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Filter",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            val categories = remember {
                getCategoriesAsList()
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(
                    items = categories,
                    key = { it.displayName }
                ) {
                    FilterChip(
                        selected = selectedCategories == it,
                        onClick = { onCategoriesClicked(it) },
                        label = {
                            Text(
                                text = it.displayName,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Transparent
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = MaterialTheme.colorScheme.primary,
                            selectedBorderColor = MaterialTheme.colorScheme.primary,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp
                        )
                    )
                }
            }
            when (restaurantResources) {
                is Resource.Error -> ErrorScreen(
                    errorMessage = restaurantResources.message
                        ?: stringResource(id = R.string.unknown_error),
                    onRetry = onRetry::invoke
                )

                Resource.Loading -> LoadingScreen()

                Resource.None -> Unit

                is Resource.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp,
                            top = 0.dp
                        )
                    ) {
                        items(
                            items = restaurantResources.data ?: emptyList(),
                            key = { restaurant ->
                                restaurant.id
                            }
                        ) { restaurant ->
                            RestaurantItem(
                                restaurant = restaurant,
                                onClicked = { restaurantID ->
                                    navController.navigate(
                                        Screen.DetailRestaurant
                                            .createRoute(restaurantID)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewListRestaurantScreen() {
    val restaurants = listOf(
        Restaurant(
            id = 1,
            title = "Tom's Kitchen",
            image = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food-composition_23-2149141352.jpg?w=1380&t=st=1685077948~exp=1685078548~hmac=dc6e59db72bc9f6dd6c9658f7e049882c1057ba1c3d1fde2f1311dec21681706"
        ),
        Restaurant(
            id = 2,
            title = "Bangkok Taste Thai Cuisine",
            image = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food_23-2149141313.jpg?t=st=1685055169~exp=1685055769~hmac=f0546d7234bba701fee3392f7c16d57c322806cfc881e19100ddeb51977ca365"
        ),
        Restaurant(
            id = 3,
            title = "Hokkaido’s House",
            image = "https://img.freepik.com/premium-photo/traditional-spanish-breakfast-tostada-with-different-toppings-dark-background_79782-3251.jpg?w=1380"
        )
    )
    TheLorryTestTheme {
        Surface {
            ListRestaurantScreen(
                navController = rememberNavController(),
                restaurantResources = Resource.Success(restaurants),
                selectedCategories = Categories.ASIAN,
                onCategoriesClicked = {},
                onRetry = {}
            )
        }
    }
}