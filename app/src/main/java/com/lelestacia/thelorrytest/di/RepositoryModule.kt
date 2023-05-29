package com.lelestacia.thelorrytest.di

import com.lelestacia.thelorrytest.data.remote.RestaurantAPI
import com.lelestacia.thelorrytest.data.repository.IRestaurantRepository
import com.lelestacia.thelorrytest.data.repository.RestaurantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        restaurantAPI: RestaurantAPI
    ): IRestaurantRepository =
        RestaurantRepository(restaurantAPI)
}