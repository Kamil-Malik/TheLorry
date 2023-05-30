package com.lelestacia.thelorrytest.domain.usecases

import com.lelestacia.thelorrytest.domain.model.DetailRestaurant
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.flow.Flow

interface IDetailRestaurantUseCases {
    fun getRestaurantDetailsByID(restaurantID: Int): Flow<Resource<DetailRestaurant>>
}