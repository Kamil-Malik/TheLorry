package com.lelestacia.thelorrytest.ui.screen.list

import com.lelestacia.thelorrytest.util.Categories

sealed class ListRestaurantScreenEvent {
    data class OnCategorySelected(val category: Categories) : ListRestaurantScreenEvent()
    object OnListRestaurantRetry : ListRestaurantScreenEvent()
}
