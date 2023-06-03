package com.lelestacia.thelorrytest.ui.screen.detail

sealed class DetailRestaurantScreenEvent {
    object OnRetryRestaurantDetailRestaurant : DetailRestaurantScreenEvent()
    object OnRetryOrLoadNextComment : DetailRestaurantScreenEvent()
    data class OnUserCommentChanged(val comment: String) : DetailRestaurantScreenEvent()
    object OnSendUserComment : DetailRestaurantScreenEvent()
}
