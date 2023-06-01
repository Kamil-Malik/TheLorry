package com.lelestacia.thelorrytest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.thelorrytest.data.model.CommentDTO
import com.lelestacia.thelorrytest.data.model.RestaurantDTO
import com.lelestacia.thelorrytest.data.remote.CommentsPagingSource
import com.lelestacia.thelorrytest.data.remote.RestaurantAPI
import com.lelestacia.thelorrytest.domain.mapper.asComment
import com.lelestacia.thelorrytest.domain.mapper.asDetailRestaurant
import com.lelestacia.thelorrytest.domain.mapper.asRestaurant
import com.lelestacia.thelorrytest.domain.model.Comment
import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.util.ErrorParserUtil
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

    override fun getRestaurantDetailsByID(restaurantID: Int): Flow<Resource<RestaurantDetail>> {
        return flow<Resource<RestaurantDetail>> {
            val apiResult = restaurantAPI.getRestaurantDetailsByID(restaurantID = restaurantID)
            emit(Resource.Success(data = apiResult.data.asDetailRestaurant()))
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

    override fun getCommentsByRestaurantID(restaurantID: Int): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 1,
                initialLoadSize = 5,
            )
        ) {
            CommentsPagingSource(restaurantID, restaurantAPI)
        }.flow.map { pagingData ->
            pagingData.map(CommentDTO::asComment)
        }
    }

    override fun getCommentsByRestaurantID(
        restaurantID: Int,
        page: Int
    ): Flow<Resource<List<Comment>>> {
        return flow<Resource<List<Comment>>> {
            val apiResult = restaurantAPI.getCommentsByRestaurantID(
                restaurantID = restaurantID,
                page = page
            )
            emit(
                Resource.Success(
                    data = apiResult.data.comments.map(CommentDTO::asComment)
                )
            )
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