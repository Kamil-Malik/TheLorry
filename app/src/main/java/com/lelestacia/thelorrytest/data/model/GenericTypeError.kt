package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json

data class GenericTypeError(
    @field:Json(name = "status")
    val status: Boolean,

    @field:Json(name = "message")
    val message: String,

    @field:Json(name = "error")
    val error: ErrorAPI
) {
    data class ErrorAPI(
        @field:Json(name = "message")
        val message: String,
    )
}