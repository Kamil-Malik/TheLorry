package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantDetailDTO(
    @Json(name = "title")
    val title: String,

    @Json(name = "images")
    val images: List<ImageUrlDTO>,

    @Json(name = "rating")
    val rating: Int,

    @Json(name = "address")
    val address: RestaurantAddressDTO,

    @Json(name = "description")
    val description: String
) {
    @JsonClass(generateAdapter = true)
    data class ImageUrlDTO(
        @Json(name = "url")
        val url: String
    )

    @JsonClass(generateAdapter = true)
    data class RestaurantAddressDTO(
        @Json(name = "full_name")
        val fullName: String,

        @Json(name = "lat")
        val lat: String,

        @Json(name = "lng")
        val lng: String
    )
}
