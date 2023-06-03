package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostCommentErrorDTO(
    @Json(name = "errors")
    val errors: List<PostCommentErrorMessageDTO>,
) {

    @JsonClass(generateAdapter = true)
    data class PostCommentErrorMessageDTO(
        @Json(name = "messages")
        val messages: String
    )
}