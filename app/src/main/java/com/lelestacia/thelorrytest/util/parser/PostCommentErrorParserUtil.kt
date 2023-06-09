package com.lelestacia.thelorrytest.util.parser

import android.content.Context
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.data.model.PostCommentErrorDTO
import com.lelestacia.thelorrytest.util.CommentTooLongException
import com.lelestacia.thelorrytest.util.CommentTooShortException
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class PostCommentErrorParserUtil(
    private val context: Context
) {

    operator fun invoke(t: Throwable): String {
        if (t is HttpException) {
            val errorResponse = t.response()?.errorBody()
            val moshi: Moshi = Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter: JsonAdapter<PostCommentErrorDTO> =
                moshi.adapter(PostCommentErrorDTO::class.java).lenient()
            return try {
                adapter.fromJson(
                    errorResponse?.string() ?: throw Exception()
                )?.errors?.get(0)?.messages ?: throw Exception()
            } catch (e: Exception) {
                context.getString(R.string.parsing_failed)
            }
        }

        if (t is UnknownHostException || t is IOException || t is TimeoutException) {
            return context.getString(R.string.no_connection)
        }

        if (t is NullPointerException) {
            return context.getString(R.string.comment_is_empty)
        }

        if (t is CommentTooShortException) {
            return context.getString(R.string.comment_too_short)
        }

        if (t is CommentTooLongException) {
            return context.getString(R.string.comment_too_long)
        }

        return t.message ?: context.getString(R.string.unknown_error)
    }
}