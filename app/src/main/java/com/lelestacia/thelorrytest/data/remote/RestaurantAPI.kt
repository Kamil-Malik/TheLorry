package com.lelestacia.thelorrytest.data.remote

import com.lelestacia.thelorrytest.data.model.DetailRestaurantDTO
import com.lelestacia.thelorrytest.data.model.RestaurantListDTO
import com.lelestacia.thelorrytest.data.model.GenericType
import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantAPI {

    @GET("restaurants/category/{category}")
    suspend fun getRestaurantsListByCategory(
        @Path("category") category: String
    ): GenericType<RestaurantListDTO>

    @GET("restaurants/{restaurant_id}")
    suspend fun getRestaurantDetailsByID(
        @Path("restaurant_id") restaurantID: Int,
    ): GenericType<DetailRestaurantDTO>
}