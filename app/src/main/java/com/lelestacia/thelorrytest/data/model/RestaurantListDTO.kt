package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json

data class RestaurantListDTO(
    @field:Json(name = "food_list")
    val restaurants: List<RestaurantDTO>
)
