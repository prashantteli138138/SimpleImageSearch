package com.prashant.simpleimagesearch.presenter

interface MainPresenter {
    fun onButtonClick(link:String,title:String,id:String)
    fun onDestroy()
    fun onBackPressed()
}