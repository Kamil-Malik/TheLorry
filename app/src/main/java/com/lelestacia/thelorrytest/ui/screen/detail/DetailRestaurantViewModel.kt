package com.lelestacia.thelorrytest.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.thelorrytest.domain.model.DetailRestaurant
import com.lelestacia.thelorrytest.domain.usecases.IDetailRestaurantUseCases
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
class DetailRestaurantViewModel @Inject constructor(
    private val useCases: IDetailRestaurantUseCases
) : ViewModel() {

    private val _detailRestaurant: MutableStateFlow<Resource<DetailRestaurant>> =
        MutableStateFlow(Resource.None)
    val detailRestaurant: StateFlow<Resource<DetailRestaurant>> = _detailRestaurant.asStateFlow()

    fun getRestaurantDetailsByID(restaurantID: Int) = viewModelScope.launch {
        useCases
            .getRestaurantDetailsByID(restaurantID = restaurantID)
            .collectLatest { detailRestaurant ->
                _detailRestaurant.update {
                    detailRestaurant
                }
            }
    }
}