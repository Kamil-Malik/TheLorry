package com.lelestacia.thelorrytest.util

import android.content.Context
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.data.model.GenericTypeError
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class ErrorParserUtil(
    private val context: Context
) {

    operator fun invoke(t: Throwable): String {
        if (t is HttpException) {
            val errorResponse = t.response()?.errorBody()
            val moshi: Moshi = Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter: JsonAdapter<GenericTypeError> =
                moshi.adapter(GenericTypeError::class.java).lenient()
            return try {
                adapter.fromJson(
                    errorResponse?.string() ?: throw Exception()
                )?.message ?: throw Exception()
            } catch (e: Exception) {
                context.getString(R.string.parsing_failed)
            }
        }

        if (t is UnknownHostException || t is IOException || t is TimeoutException) {
            return context.getString(R.string.no_connection)
        }

        return t.message ?: context.getString(R.string.unknown_error)
    }
}