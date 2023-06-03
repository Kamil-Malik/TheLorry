package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenericType<T>(
    @Json(name = "status")
    val status: Boolean,

    @Json(name = "message")
    val message: String,

    @Json(name = "data")
    val data: T
)
