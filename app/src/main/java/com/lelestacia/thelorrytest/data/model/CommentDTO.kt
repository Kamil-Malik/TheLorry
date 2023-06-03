package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentDTO(

    @Json(name = "id")
    val id: Int,

    @Json(name = "user_name")
    val userName: String,

    @Json(name = "body")
    val body: String,

    @Json(name = "profile_picture")
    val profilePicture: String
)