package com.lelestacia.thelorrytest.domain.mapper

import com.lelestacia.thelorrytest.data.model.CommentDTO
import com.lelestacia.thelorrytest.domain.model.Comment

fun CommentDTO.asComment() =
    Comment(
        id = id,
        userName = userName,
        body = body,
        profilePicture = profilePicture
    )