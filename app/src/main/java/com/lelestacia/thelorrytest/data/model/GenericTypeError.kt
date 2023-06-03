package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenericTypeError(
    @Json(name = "status")
    val status: Boolean,

    @Json(name = "message")
    val message: String,

    @Json(name = "error")
    val error: ErrorAPI
) {

    @JsonClass(generateAdapter = true)
    data class ErrorAPI(
        @Json(name = "message")
        val message: String,
    )
}