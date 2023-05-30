package com.lelestacia.thelorrytest.domain.model

data class DetailRestaurant(
    val title: String,
    val images: List<ImageUrl>,
    val rating: Int,
    val address: RestaurantAddress,
    val description: String
) {
    data class ImageUrl(
        val url: String
    )

    data class RestaurantAddress(
        val fullName: String,
        val lat: String,
        val lng: String
    )
}
