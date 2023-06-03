package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostCommentDTO(

    @Json(name = "restaurantId")
    val id: Int,

    @Json(name = "message")
    val message: String,
)