package com.lelestacia.thelorrytest.ui.screen.list

import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.util.Categories
import com.lelestacia.thelorrytest.util.Resource

data class ListRestaurantScreenState(
    val selectedCategory: Categories = Categories.ASIAN,
    val restaurants: Resource<List<Restaurant>> = Resource.None,
)
