package com.lelestacia.thelorrytest.di

import android.content.Context
import com.lelestacia.thelorrytest.data.remote.RestaurantAPI
import com.lelestacia.thelorrytest.data.repository.IRestaurantRepository
import com.lelestacia.thelorrytest.data.repository.RestaurantRepository
import com.lelestacia.thelorrytest.util.ErrorParserUtil
import com.lelestacia.thelorrytest.util.PostCommentErrorParserUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        restaurantAPI: RestaurantAPI,
        @ApplicationContext context: Context
    ): IRestaurantRepository =
        RestaurantRepository(
            restaurantAPI = restaurantAPI,
            errorParserUtil = ErrorParserUtil(context),
            postCommentErrorParser = PostCommentErrorParserUtil(context)
        )
}