package com.prashant.simpleimagesearch.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prashant.simpleimagesearch.Interfaces.OnItemClickListener
import com.prashant.simpleimagesearch.R
import com.prashant.simpleimagesearch.model.ImageDetails
import java.util.*


class ImageListAdapter(
    private val mContext: Context,
    private val cat: ArrayList<ImageDetails>?,
    private val mItemClickListener: OnItemClickListener?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var imageView: ImageView? = null
    var title: TextView? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_layout, parent, false)
        return PackageHolder(view)
    }

    override fun onBindViewHolder(
        holder1: RecyclerView.ViewHolder,
        listPosition: Int
    ) {
        val holder = holder1 as PackageHolder
        title = holder.title
        imageView = holder.imageView
        title!!.text = cat!![listPosition].title //medicine Name

        Glide.with(mContext)
            .load(cat[listPosition].link)
            .thumbnail(0.25f)
            .into(imageView)
    }

    override fun getItemCount(): Int {
        return cat?.size ?: 0
    }

    inner class PackageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title: TextView
        var imageView: ImageView
        override fun onClick(v: View) {
            val id = v.id
            mItemClickListener?.onItemClick(v, layoutPosition)
        }

        init {
            this.title = itemView.findViewById<View>(R.id.cate_name) as TextView
            this.imageView =
                itemView.findViewById<View>(R.id.cate_image) as ImageView
            itemView.setOnClickListener(this)
        }
    }

}
