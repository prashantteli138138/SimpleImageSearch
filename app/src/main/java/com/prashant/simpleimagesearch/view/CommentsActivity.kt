package com.prashant.simpleimagesearch.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.prashant.simpleimagesearch.R
import com.prashant.simpleimagesearch.database.DB
import com.prashant.simpleimagesearch.model.ImageDetails
import com.prashant.simpleimagesearch.presenter_comments.CommentsPresenter
import com.prashant.simpleimagesearch.presenter_comments.CommentsPresenterImpl

class CommentsActivity : AppCompatActivity() {
    lateinit var imageView: ImageView;
    lateinit var comment: EditText;
    lateinit var imageDetails: ImageDetails
    lateinit var commentsPresenter: CommentsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        init()
    }

    fun onSubmit(view: View) {
        imageDetails.comment = comment.text.toString()
        commentsPresenter.AddComment(imageDetails)
        Toast.makeText(applicationContext, "Comment Added Successfully", Toast.LENGTH_LONG).show()
    }

    private fun init() {
        imageView = findViewById<ImageView>(R.id.imageView);
        comment = findViewById<EditText>(R.id.editText);
        commentsPresenter = CommentsPresenterImpl(this)
        val bundle = intent.extras
        val id = bundle?.get("id")
        val title = bundle?.get("title")
        val link = bundle?.get("link")
        val previousComment = commentsPresenter.getCommentById("ItemId", id as String)

        if (previousComment != null) {
            if (previousComment.comment != null) {
                comment.setText(previousComment.comment)
            }
        }
        imageDetails = ImageDetails()
        imageDetails.id = id as String?
        imageDetails.title = title as String?
        imageDetails.link = link as String?

        Glide.with(this)
            .load(link)
            .thumbnail( /*sizeMultiplier=*/0.25f)
            .into(imageView)

        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        toolbar.setNavigationIcon(resources.getDrawable(R.drawable.ic_arrow_back))

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener(View.OnClickListener { onBackPressed() })

    }

}
