package com.prashant.simpleimagesearch.presenter

import androidx.activity.OnBackPressedCallback

interface MainPresenter {
    fun onButtonClick(link:String,title:String,id:String)
    fun onDestroy()
    fun onBackPressed(callback: OnBackPressedCallback)
}