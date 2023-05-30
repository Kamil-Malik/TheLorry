package com.lelestacia.thelorrytest.domain.mapper

import com.lelestacia.thelorrytest.data.model.DetailRestaurantDTO
import com.lelestacia.thelorrytest.data.model.RestaurantDTO
import com.lelestacia.thelorrytest.domain.model.DetailRestaurant
import com.lelestacia.thelorrytest.domain.model.Restaurant

fun RestaurantDTO.asRestaurant() =
    Restaurant(
        id = id,
        title = title,
        image = image
    )

fun DetailRestaurantDTO.asDetailRestaurant() =
    DetailRestaurant(
        title,
        images = images.map {
            DetailRestaurant.ImageUrl(it.url)
        },
        rating = rating,
        address = DetailRestaurant.RestaurantAddress(
            fullName = address.fullName,
            lat = address.lat,
            lng = address.lng
        ),
        description = description
    )