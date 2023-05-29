package com.lelestacia.thelorrytest.ui.screen.list

import androidx.lifecycle.ViewModel
import com.lelestacia.thelorrytest.domain.usecases.IListRestaurantUseCases
import com.lelestacia.thelorrytest.util.Categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ListRestaurantViewModel @Inject constructor(
    private val useCases: IListRestaurantUseCases
) : ViewModel() {

    private val _selectedCategories: MutableStateFlow<Categories> = MutableStateFlow(Categories.ASIAN)
    val selectedCategories: StateFlow<Categories> = _selectedCategories.asStateFlow()

    val restaurants = _selectedCategories
        .flatMapLatest {
            useCases.getRestaurantsListByCategory(category = it.queryName)
    }

    fun onCategoriesChanged(categories: Categories) {
        _selectedCategories.update { categories }
    }
}