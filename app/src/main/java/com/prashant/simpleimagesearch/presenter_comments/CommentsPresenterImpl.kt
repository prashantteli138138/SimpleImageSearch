package com.prashant.simpleimagesearch.presenter_comments

import android.content.Context
import android.widget.Toast
import com.prashant.simpleimagesearch.database.DB
import com.prashant.simpleimagesearch.model.ImageDetails

class CommentsPresenterImpl : CommentsPresenter {
    lateinit var db: DB;
    val context: Context

    constructor(context: Context) {
        this.context = context
        db = DB(context)
    }

    override fun getCommentById(ItemId: String, id: String): ImageDetails? {
        val previousComment = db.getCommentById("ItemId", id as String?)
        return previousComment
    }

    override fun AddComment(imageDetails: ImageDetails) {
        db.AddComment(imageDetails)
        Toast.makeText(context, "Comment Added Successfully", Toast.LENGTH_LONG).show()
    }


}