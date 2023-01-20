package com.example.webviewwithpdf.pdf_as_bitmap

import android.content.res.AssetManager
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumayank.airpdf.helpers.AssetsHelper
import com.mumayank.airpdf.helpers.FileDownloadHelper
import com.mumayank.airpdf.helpers.PdfHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class PdfAsBitmapViewModel : ViewModel() {

    private val _currentIndex: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }
    val currentIndex: LiveData<Int> = _currentIndex

    private val _lastIndex: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }
    val lastIndex: LiveData<Int> = _lastIndex

    private val _bitmap: MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
    }
    val bitmap: LiveData<Bitmap> = _bitmap

    private val _error: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val error: LiveData<Boolean> = _error

    private lateinit var viewMeasurement: Pair<Int, Int>
    private lateinit var filename: String
    private lateinit var cacheDir: File

    fun onPreviousPage() {
        onPageChange(false)
    }

    fun onNextPage() {
        onPageChange(true)
    }

    private fun onPageChange(isNextPage: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val addOrSub = if (isNextPage) 1 else -1
                _currentIndex.value = (currentIndex.value ?: 0) + addOrSub
                getBitmap()
            }
        }
    }

    fun setViewMeasurements(width: Int) {
        viewModelScope.launch {
            viewMeasurement = PdfHelper.getImageViewMeasurements(width)
        }
    }

    fun loadFirstPdfPage(cacheDir: File, url: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val fileName = FileDownloadHelper.downloadFromUrlAndGetName(cacheDir, url)
                if (fileName.isNullOrEmpty()) {
                    _error.postValue(true)
                    return@withContext
                }
                filename = fileName
                _lastIndex.postValue(PdfHelper.getLastIndex(cacheDir, filename))
                this@PdfAsBitmapViewModel.cacheDir = cacheDir
                getBitmap()
            }
        }
    }

    fun loadFirstPdfPage(cacheDir: File, assetManager: AssetManager, assetFileName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val inputStream = AssetsHelper.getInputStreamFromAssets(assetManager, assetFileName)
                if (inputStream == null) {
                    _error.postValue(true)
                    return@withContext
                }

                val file = AssetsHelper.getFileFromInputStream(cacheDir, inputStream)
                if (file == null) {
                    _error.postValue(true)
                    return@withContext
                }

                filename = file.name
                _lastIndex.postValue(PdfHelper.getLastIndex(cacheDir, filename))
                this@PdfAsBitmapViewModel.cacheDir = cacheDir
                getBitmap()
            }
        }
    }

    private suspend fun getBitmap() {
        withContext(Dispatchers.Main) {
            val bitmap =
                PdfHelper.getBitmap(cacheDir, filename, viewMeasurement, (currentIndex.value ?: 0))
            if (bitmap == null) {
                _error.postValue(true)
                return@withContext
            }
            _bitmap.postValue(bitmap)
        }
    }

    fun resetError() {
        _error.postValue(false)
    }

    fun deleteDownloadedFiles(cacheDir: File) {
        viewModelScope.launch {
            FileDownloadHelper.deleteAllDownloadedFiles(cacheDir)
        }
    }
}