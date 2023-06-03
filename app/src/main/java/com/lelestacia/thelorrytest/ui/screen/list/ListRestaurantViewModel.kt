package com.lelestacia.thelorrytest.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.domain.usecases.IListRestaurantUseCases
import com.lelestacia.thelorrytest.util.Categories
import com.lelestacia.thelorrytest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListRestaurantViewModel @Inject constructor(
    private val useCases: IListRestaurantUseCases
) : ViewModel() {

    private val _selectedCategories: MutableStateFlow<Categories> =
        MutableStateFlow(Categories.ASIAN)
    private val _restaurants: MutableStateFlow<Resource<List<Restaurant>>> =
        MutableStateFlow(Resource.None)
    val listRestaurantScreenState = combine(
        flow = _selectedCategories,
        flow2 = _restaurants
    ) { selectedCategory, restaurants ->
        ListRestaurantScreenState(
            selectedCategory = selectedCategory,
            restaurants = restaurants
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, ListRestaurantScreenState())

    fun onEvent(event: ListRestaurantScreenEvent) = viewModelScope.launch {
        when (event) {
            is ListRestaurantScreenEvent.OnCategorySelected -> {
                _selectedCategories.update { event.category }
                getRestaurantsListByCategory()
            }

            ListRestaurantScreenEvent.OnListRestaurantRetry -> getRestaurantsListByCategory()
        }
    }

    private fun getRestaurantsListByCategory() = viewModelScope.launch {
        useCases.getRestaurantsListByCategory(
            category = _selectedCategories.value.queryName
        ).collectLatest { result ->
            _restaurants.update { result }
        }
    }

    init {
        getRestaurantsListByCategory()
    }
}