package com.lelestacia.thelorrytest.data.repository

import com.lelestacia.thelorrytest.data.model.RestaurantDTO
import com.lelestacia.thelorrytest.data.remote.RestaurantAPI
import com.lelestacia.thelorrytest.domain.mapper.asRestaurant
import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.util.ErrorParserUtil
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RestaurantRepository @Inject constructor(
    private val restaurantAPI: RestaurantAPI,
    private val errorParserUtil: ErrorParserUtil = ErrorParserUtil(),
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) : IRestaurantRepository {

    override fun getRestaurantsListByCategory(category: String): Flow<Resource<List<Restaurant>>> {
        return flow<Resource<List<Restaurant>>> {
            val apiResult = restaurantAPI.getRestaurantsListByCategory(category = category)
            emit(Resource.Success(data = apiResult.data.restaurants.map(RestaurantDTO::asRestaurant)))
        }.onStart {
            emit(Resource.Loading)
        }.catch {
            emit(
                Resource.Error(
                    data = null,
                    message = errorParserUtil(it)
                )
            )
        }.flowOn(ioDispatcher)
    }
}