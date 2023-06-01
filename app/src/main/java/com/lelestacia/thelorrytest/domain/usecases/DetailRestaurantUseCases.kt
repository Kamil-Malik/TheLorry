package com.lelestacia.thelorrytest.domain.usecases

import androidx.paging.PagingData
import com.lelestacia.thelorrytest.data.repository.IRestaurantRepository
import com.lelestacia.thelorrytest.domain.model.Comment
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailRestaurantUseCases @Inject constructor(
    private val repository: IRestaurantRepository
) : IDetailRestaurantUseCases {

    override fun getRestaurantDetailsByID(restaurantID: Int): Flow<Resource<RestaurantDetail>> {
        return repository.getRestaurantDetailsByID(restaurantID = restaurantID)
    }

    override fun getCommentsByRestaurantID(restaurantID: Int): Flow<PagingData<Comment>> {
        return repository.getCommentsByRestaurantID(restaurantID)
    }

    override fun getCommentsByRestaurantID(
        restaurantID: Int,
        page: Int
    ): Flow<Resource<List<Comment>>> {
        return repository.getCommentsByRestaurantID(
            restaurantID = restaurantID,
            page = page
        )
    }
}