package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentsDTO(
    @Json(name = "comments")
    val comments: List<CommentDTO>
)
