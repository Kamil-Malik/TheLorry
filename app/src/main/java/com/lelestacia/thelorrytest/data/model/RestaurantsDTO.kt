package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantsDTO(
    @Json(name = "food_list")
    val restaurants: List<RestaurantDTO>
)
