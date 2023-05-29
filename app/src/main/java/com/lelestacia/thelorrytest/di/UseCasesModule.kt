package com.lelestacia.thelorrytest.di

import com.lelestacia.thelorrytest.data.repository.IRestaurantRepository
import com.lelestacia.thelorrytest.domain.usecases.IListRestaurantUseCases
import com.lelestacia.thelorrytest.domain.usecases.ListRestaurantUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @ViewModelScoped
    @Provides
    fun provideListRestaurantUseCases(
        repository: IRestaurantRepository
    ): IListRestaurantUseCases =
        ListRestaurantUseCases(repository)
}