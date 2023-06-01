package com.lelestacia.thelorrytest.domain.usecases

import androidx.paging.PagingData
import com.lelestacia.thelorrytest.domain.model.Comment
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.flow.Flow

interface IDetailRestaurantUseCases {
    fun getRestaurantDetailsByID(restaurantID: Int): Flow<Resource<RestaurantDetail>>
    fun getCommentsByRestaurantID(restaurantID: Int): Flow<PagingData<Comment>>
    fun getCommentsByRestaurantID(restaurantID: Int, page: Int): Flow<Resource<List<Comment>>>
}