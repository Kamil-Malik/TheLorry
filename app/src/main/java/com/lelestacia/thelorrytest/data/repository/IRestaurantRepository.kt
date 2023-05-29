package com.lelestacia.thelorrytest.data.repository

import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.flow.Flow

interface IRestaurantRepository {
    fun getRestaurantsListByCategory(category: String): Flow<Resource<List<Restaurant>>>
}