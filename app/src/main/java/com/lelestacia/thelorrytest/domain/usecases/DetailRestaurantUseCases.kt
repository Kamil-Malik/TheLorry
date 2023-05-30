package com.lelestacia.thelorrytest.domain.usecases

import com.lelestacia.thelorrytest.data.repository.IRestaurantRepository
import com.lelestacia.thelorrytest.domain.model.DetailRestaurant
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailRestaurantUseCases @Inject constructor(
    private val repository: IRestaurantRepository
) : IDetailRestaurantUseCases {

    override fun getRestaurantDetailsByID(restaurantID: Int): Flow<Resource<DetailRestaurant>> {
        return repository.getRestaurantDetailsByID(restaurantID = restaurantID)
    }
}