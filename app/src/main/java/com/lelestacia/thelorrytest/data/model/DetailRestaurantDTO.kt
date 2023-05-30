package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailRestaurantDTO(
    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "images")
    val images: List<ImageUrlDTO>,

    @field:Json(name = "rating")
    val rating: Int,

    @field:Json(name = "address")
    val address: RestaurantAddressDTO,

    @field:Json(name = "description")
    val description: String
) {
    @JsonClass(generateAdapter = true)
    data class ImageUrlDTO(
        @field:Json(name = "url")
        val url: String
    )

    @JsonClass(generateAdapter = true)
    data class RestaurantAddressDTO(
        @field:Json(name = "full_name")
        val fullName: String,

        @field:Json(name = "lat")
        val lat: String,

        @field:Json(name = "lng")
        val lng: String
    )
}
