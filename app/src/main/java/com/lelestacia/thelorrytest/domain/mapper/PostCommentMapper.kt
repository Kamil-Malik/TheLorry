package com.lelestacia.thelorrytest.domain.mapper

import com.lelestacia.thelorrytest.data.model.PostCommentDTO
import com.lelestacia.thelorrytest.domain.model.PostComment

fun PostComment.asPostCommentDTO() =
    PostCommentDTO(
        id = restaurantID,
        message = message
    )