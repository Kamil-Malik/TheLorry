package com.lelestacia.thelorrytest.domain.usecases

import com.lelestacia.thelorrytest.data.repository.IRestaurantRepository
import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListRestaurantUseCases @Inject constructor(
    private val repository: IRestaurantRepository
) : IListRestaurantUseCases {

    override fun getRestaurantsListByCategory(category: String): Flow<Resource<List<Restaurant>>> {
        return repository.getRestaurantsListByCategory(category = category)
    }
}