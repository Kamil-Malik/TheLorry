package com.lelestacia.thelorrytest.domain.mapper

import com.lelestacia.thelorrytest.data.model.RestaurantDTO
import com.lelestacia.thelorrytest.domain.model.Restaurant

fun RestaurantDTO.asRestaurant() =
    Restaurant(
        id = id,
        title = title,
        image = image
    )