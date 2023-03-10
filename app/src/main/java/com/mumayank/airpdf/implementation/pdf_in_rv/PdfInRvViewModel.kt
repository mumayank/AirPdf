package com.mumayank.airpdf.implementation.pdf_in_rv

import android.content.res.AssetManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumayank.airpdf.helpers.PdfHelper
import kotlinx.coroutines.launch
import java.io.File

class PdfInRvViewModel : ViewModel() {

    private val _bitmapFilenames: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }
    val bitmapFilenames: LiveData<List<String>> = _bitmapFilenames

    fun updateBitmapFilename(
        dir: File,
        assetManager: AssetManager,
        assetFilename: String,
        width: Int
    ) {
        viewModelScope.launch {
            _bitmapFilenames.value = PdfHelper.getBitmapFilenames(
                dir,
                assetManager,
                assetFilename,
                width
            )
        }
    }

    fun updateBitmapFilename(
        dir: File,
        url: String,
        width: Int
    ) {
        viewModelScope.launch {
            _bitmapFilenames.value = PdfHelper.getBitmapFilenames(
                dir,
                url,
                width
            )
        }
    }

}