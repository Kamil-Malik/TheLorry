package com.lelestacia.thelorrytest.di

import com.lelestacia.thelorrytest.data.remote.RestaurantAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideEndPoint(
        retrofit: Retrofit
    ): RestaurantAPI = retrofit.create(RestaurantAPI::class.java)

    private const val BASE_URL = "https://59z6gmj54k.execute-api.ap-southeast-1.amazonaws.com/dev/"
}