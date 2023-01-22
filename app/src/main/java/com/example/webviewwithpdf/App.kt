package com.example.webviewwithpdf

import android.app.Application

class App : Application() {

    private var bitmapFilenames = arrayListOf<String>()

    fun updateBitmapFilenames(bitmapFilenames: List<String> = listOf()) {
        this.bitmapFilenames.apply {
            clear()
            addAll(bitmapFilenames)
        }
    }

    fun getBitmapFilenames() = bitmapFilenames

}