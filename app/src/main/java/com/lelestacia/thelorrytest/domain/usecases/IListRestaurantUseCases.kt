package com.lelestacia.thelorrytest.domain.usecases

import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.flow.Flow

interface IListRestaurantUseCases {
    fun getRestaurantsListByCategory(category: String): Flow<Resource<List<Restaurant>>>
}