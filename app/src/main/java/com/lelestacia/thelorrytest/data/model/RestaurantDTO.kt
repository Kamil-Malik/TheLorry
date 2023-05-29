package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantDTO(
    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "image")
    val image: String
)