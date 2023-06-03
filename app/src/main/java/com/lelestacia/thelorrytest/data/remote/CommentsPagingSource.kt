package com.lelestacia.thelorrytest.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.thelorrytest.data.model.CommentDTO

class CommentsPagingSource(
    private val restaurantID: Int,
    private val restaurantAPI: RestaurantAPI
) : PagingSource<Int, CommentDTO>() {

    override fun getRefreshKey(state: PagingState<Int, CommentDTO>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentDTO> {
        return try {
            val page = params.key ?: 1
            val apiResult = restaurantAPI.getCommentsByRestaurantID(
                restaurantID = restaurantID,
                page = page
            )
            LoadResult.Page(
                apiResult.data.comments,
                if (page == 1) {
                    null
                } else {
                    page - 1
                },
                nextKey =
                if(apiResult.data.comments.isEmpty()) {
                    null
                } else {
                    page + 1
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}