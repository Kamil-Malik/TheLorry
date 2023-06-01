package com.lelestacia.thelorrytest.data.remote

import com.lelestacia.thelorrytest.data.model.CommentsDTO
import com.lelestacia.thelorrytest.data.model.RestaurantDetailDTO
import com.lelestacia.thelorrytest.data.model.RestaurantsDTO
import com.lelestacia.thelorrytest.data.model.GenericType
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantAPI {

    @GET("restaurants/category/{category}")
    suspend fun getRestaurantsListByCategory(
        @Path("category") category: String
    ): GenericType<RestaurantsDTO>

    @GET("restaurants/{restaurant_id}")
    suspend fun getRestaurantDetailsByID(
        @Path("restaurant_id") restaurantID: Int,
    ): GenericType<RestaurantDetailDTO>

    @GET("restaurants/{restaurant_id}/comments")
    suspend fun getCommentsByRestaurantID(
        @Path("restaurant_id") restaurantID: Int,
        @Query("page") page: Int
    ): GenericType<CommentsDTO>
}