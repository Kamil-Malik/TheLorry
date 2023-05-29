package com.lelestacia.thelorrytest.util

sealed class Screen(val route: String) {
    object ListRestaurant : Screen("/")
    object DetailRestaurant : Screen("/{restaurant_id}") {
        fun createRoute(restaurantID: Int): String {
            return this.route.replace(
                "{restaurant_id}",
                restaurantID.toString()
            )
        }
    }
}
