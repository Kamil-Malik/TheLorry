package com.lelestacia.thelorrytest.ui.screen.detail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.thelorrytest.domain.model.Comment
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
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

    private val _restaurantID: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _restaurantDetail: MutableStateFlow<Resource<RestaurantDetail>> =
        MutableStateFlow(Resource.None)
    val restaurantDetail: StateFlow<Resource<RestaurantDetail>> = _restaurantDetail.asStateFlow()

    private val currentPage: MutableStateFlow<Int> = MutableStateFlow(1)

    private val _hasNextPage: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val hasNextPage: StateFlow<Boolean> = _hasNextPage.asStateFlow()

    private val _commentsLoadState: MutableStateFlow<Resource<Any>> =
        MutableStateFlow(Resource.None)
    val commentLoadState: StateFlow<Resource<Any>> = _commentsLoadState.asStateFlow()

    val comments: SnapshotStateList<Comment> = mutableStateListOf()

    fun updateRestaurantID(restaurantID: Int) = viewModelScope.launch {
        _restaurantID.update { restaurantID }
    }

    fun getRestaurantDetailsByID(restaurantID: Int) = viewModelScope.launch {
        useCases
            .getRestaurantDetailsByID(restaurantID = restaurantID)
            .collectLatest { detailRestaurant ->
                _restaurantDetail.update {
                    detailRestaurant
                }
            }
    }

    fun fetchComment() = viewModelScope.launch {
        useCases.getCommentsByRestaurantID(
            restaurantID = _restaurantID.value,
            page = currentPage.value
        ).collectLatest { resource ->
            _commentsLoadState.update { resource }

            if (resource is Resource.Success) {
                currentPage.update { it + 1 }

                if (resource.data.isNullOrEmpty()) {
                    _hasNextPage.update { false }
                    return@collectLatest
                }

                resource.data.forEach { comment ->
                    comments.add(comment)
                }
            }
        }
    }
}