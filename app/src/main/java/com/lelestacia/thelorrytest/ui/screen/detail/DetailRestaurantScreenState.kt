package com.lelestacia.thelorrytest.ui.screen.detail

import com.lelestacia.thelorrytest.domain.model.Comment
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.util.Resource

data class DetailRestaurantScreenState(
    val restaurantDetail: Resource<RestaurantDetail> = Resource.None,
    val hasNextPage: Boolean = true,
    val restaurantDetailComments: Pair<List<Comment>, Resource<Any>> = Pair(
        first = listOf(),
        second = Resource.None
    ),
    val userComment: String = "",
)
