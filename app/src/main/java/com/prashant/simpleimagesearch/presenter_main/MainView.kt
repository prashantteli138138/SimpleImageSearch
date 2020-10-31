package com.prashant.simpleimagesearch.presenter_main

import com.prashant.simpleimagesearch.model.ImageDetails

interface MainView {
    fun notifyDataSetChanged()
    fun addData(res: ImageDetails)
}