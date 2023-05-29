package com.lelestacia.thelorrytest.util

import com.lelestacia.thelorrytest.data.model.GenericTypeError
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class ErrorParserUtil {
    operator fun invoke(t: Throwable): String {
        if (t is HttpException) {
            val errorResponse = t.response()?.errorBody()
            val moshi: Moshi = Moshi
                .Builder()
                .add(MoshiConverterFactory.create())
                .build()
            val adapter: JsonAdapter<GenericTypeError> = moshi.adapter(GenericTypeError::class.java)
            return try {
                adapter.fromJson(
                    errorResponse?.string() ?: throw Exception("Failed to catch the response.")
                )?.message ?: throw Exception("Response failed to parse.")
            } catch (e: Exception) {
                e.message ?: "Response failed to parse."
            }
        }

        if (t is UnknownHostException || t is IOException || t is TimeoutException) {
            return "Please check your connection and try again."
        }

        return t.message ?: "Unknown Error."
    }
}