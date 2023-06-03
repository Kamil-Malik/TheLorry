package com.lelestacia.thelorrytest.domain.mapper

import com.lelestacia.thelorrytest.data.model.RestaurantDetailDTO
import com.lelestacia.thelorrytest.data.model.RestaurantDTO
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.domain.model.Restaurant

fun RestaurantDTO.asRestaurant() =
    Restaurant(
        id = id,
        title = title,
        image = image
    )

fun RestaurantDetailDTO.asDetailRestaurant() =
    RestaurantDetail(
        title,
        images = images.map {
            RestaurantDetail.ImageUrl(it.url)
        },
        rating = rating,
        address = RestaurantDetail.RestaurantAddress(
            fullName = address.fullName,
            lat = address.lat,
            lng = address.lng
        ),
        description = description
    )