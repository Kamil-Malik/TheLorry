package com.lelestacia.thelorrytest.data.repository

import androidx.paging.PagingData
import com.lelestacia.thelorrytest.domain.model.Comment
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.flow.Flow

interface IRestaurantRepository {
    fun getRestaurantsListByCategory(category: String): Flow<Resource<List<Restaurant>>>
    fun getRestaurantDetailsByID(restaurantID: Int): Flow<Resource<RestaurantDetail>>
    fun getCommentsByRestaurantID(restaurantID: Int): Flow<PagingData<Comment>>
    fun getCommentsByRestaurantID(restaurantID: Int, page: Int): Flow<Resource<List<Comment>>>
}