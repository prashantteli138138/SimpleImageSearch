package com.prashant.simpleimagesearch.presenter_main

import android.os.IBinder
import androidx.activity.OnBackPressedCallback

interface MainPresenter {
    fun onButtonClick(link: String, title: String, id: String)
    fun onDestroy()
    fun onBackPressed(callback: OnBackPressedCallback)
    fun MakeAPICall(queryParams: String)
    fun hideKeyboard(binder: IBinder)
    fun SetPageNo(pageNo: Int)
}