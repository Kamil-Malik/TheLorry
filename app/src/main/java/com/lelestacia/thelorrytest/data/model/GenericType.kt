package com.lelestacia.thelorrytest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenericType<T>(
    @field:Json(name = "status")
    val status: Boolean,

    @field:Json(name = "message")
    val message: String,

    @field:Json(name = "data")
    val data: T
)
