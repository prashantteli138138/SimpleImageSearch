package com.prashant.simpleimagesearch.presenter_comments

import com.prashant.simpleimagesearch.model.ImageDetails

interface CommentsPresenter {
    fun getCommentById(ItemId: String, id: String): ImageDetails?
    fun AddComment(imageDetails: ImageDetails)
}