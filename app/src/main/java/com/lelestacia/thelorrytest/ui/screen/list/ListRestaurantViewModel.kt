package com.lelestacia.thelorrytest.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.thelorrytest.domain.model.Restaurant
import com.lelestacia.thelorrytest.domain.usecases.IListRestaurantUseCases
import com.lelestacia.thelorrytest.util.Categories
import com.lelestacia.thelorrytest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListRestaurantViewModel @Inject constructor(
    private val useCases: IListRestaurantUseCases
) : ViewModel() {

    private val _selectedCategories: MutableStateFlow<Categories> =
        MutableStateFlow(Categories.ASIAN)
    val selectedCategories: StateFlow<Categories> = _selectedCategories.asStateFlow()

    private val _restaurants: MutableStateFlow<Resource<List<Restaurant>>> =
        MutableStateFlow(Resource.None)
    val restaurants: StateFlow<Resource<List<Restaurant>>> = _restaurants.asStateFlow()

    fun onCategoriesChanged(categories: Categories) {
        _selectedCategories.update { categories }
        getRestaurantsListByCategory()
    }

    fun getRestaurantsListByCategory() = viewModelScope.launch {
        useCases.getRestaurantsListByCategory(
            category = selectedCategories.value.queryName
        ).collectLatest { result ->
            _restaurants.update { result }
        }
    }

    init {
        getRestaurantsListByCategory()
    }
}