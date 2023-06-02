package com.lelestacia.thelorrytest.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.thelorrytest.domain.model.Comment
import com.lelestacia.thelorrytest.domain.model.PostComment
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.domain.usecases.IDetailRestaurantUseCases
import com.lelestacia.thelorrytest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailRestaurantViewModel @Inject constructor(
    private val useCases: IDetailRestaurantUseCases
) : ViewModel() {

    private val _restaurantID: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _currentPage: MutableStateFlow<Int> = MutableStateFlow(1)
    private val _restaurantDetail: MutableStateFlow<Resource<RestaurantDetail>> =
        MutableStateFlow(Resource.None)
    private val _hasNextPage: MutableStateFlow<Boolean> =
        MutableStateFlow(true)
    private val _comments: MutableStateFlow<List<Comment>> =
        MutableStateFlow(emptyList())
    private val _commentsLoadState: MutableStateFlow<Resource<Any>> =
        MutableStateFlow(Resource.None)
    private val _userComment: MutableStateFlow<String> = MutableStateFlow("")

    val detailRestaurantScreenState: StateFlow<DetailRestaurantScreenState> = combine(
        flow = _restaurantDetail,
        flow2 = _hasNextPage,
        flow3 = _comments,
        flow4 = _commentsLoadState,
        flow5 = _userComment
    ) { restaurantDetail, hasNextPage, comments, commentsLoadState, userComment ->
        DetailRestaurantScreenState(
            restaurantDetail, hasNextPage, Pair(comments, commentsLoadState), userComment
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, DetailRestaurantScreenState())

    fun onEvent(event: DetailRestaurantScreenEvent) = viewModelScope.launch {
        when (event) {
            DetailRestaurantScreenEvent.OnRetryOrLoadNextComment -> fetchComment()
            DetailRestaurantScreenEvent.OnRetryRestaurantDetailRestaurant -> getRestaurantDetailsByID(
                _restaurantID.value
            )

            DetailRestaurantScreenEvent.OnSendUserComment -> useCases.sendCommentToRestaurantByID(
                PostComment(
                    restaurantID = _restaurantID.value,
                    message = _userComment.value
                )
            )

            is DetailRestaurantScreenEvent.OnUserCommentChanged -> _userComment.update {
                event.comment
            }
        }
    }

    fun updateRestaurantID(restaurantID: Int) = viewModelScope.launch {
        val currentRestaurantID = _restaurantID.updateAndGet { restaurantID }
        getRestaurantDetailsByID(restaurantID = currentRestaurantID)
        fetchComment()
    }

    private fun getRestaurantDetailsByID(restaurantID: Int) = viewModelScope.launch {
        useCases
            .getRestaurantDetailsByID(restaurantID = restaurantID)
            .collectLatest { detailRestaurant ->
                _restaurantDetail.update {
                    detailRestaurant
                }
            }
    }

    private fun fetchComment() = viewModelScope.launch {
        useCases.getCommentsByRestaurantID(
            restaurantID = _restaurantID.value,
            page = _currentPage.value
        ).collectLatest { resource ->
            _commentsLoadState.update { resource }

            if (resource is Resource.Success) {
                _currentPage.update { it + 1 }

                if (resource.data.isNullOrEmpty()) {
                    _hasNextPage.update { false }
                    return@collectLatest
                }

                _comments.update { currentComment ->
                    val currentMutableComment = currentComment.toMutableList()
                    resource.data.forEach { comment ->
                        currentMutableComment.add(comment)
                    }
                    currentMutableComment
                }
            }
        }
    }
}