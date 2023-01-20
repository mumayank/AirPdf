package com.mumayank.airpdf.helpers

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object PdfHelper {

    suspend fun getBitmap(
        cacheDir: File,
        filename: String,
        viewMeasurement: Pair<Int, Int>,
        index: Int
    ): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(cacheDir, filename)
                val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val renderer = PdfRenderer(pfd)
                val bitmap = Bitmap.createBitmap(
                    viewMeasurement.second,
                    viewMeasurement.first,
                    Bitmap.Config.ARGB_8888
                )
                val page = renderer.openPage(index)
                bitmap?.let {
                    page.render(it, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                }
                page.close()
                renderer.close()
                bitmap
            } catch (e: Exception) {
                Log.e(LOG, e.toString())
                null
            }
        }
    }

    suspend fun getLastIndex(
        cacheDir: File,
        filename: String
    ): Int {
        return withContext(Dispatchers.IO) {
            val file = File(cacheDir, filename)
            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(pfd)
            var lastIndex = 0
            var page: PdfRenderer.Page
            try {
                while (true) {
                    page = renderer.openPage(lastIndex)
                    page.close()
                    lastIndex++
                }
            } catch (e: java.lang.Exception) {
                Log.e(LOG, e.toString())
            } finally {
                renderer.close()
            }
            --lastIndex
        }
    }

    suspend fun getImageViewMeasurements(width: Int): Pair<Int, Int> {
        return withContext(Dispatchers.Default) {
            val a4ratio = 1.414
            val imageViewA4Height = width * a4ratio
            val newHeight = imageViewA4Height.toInt()
            Pair(newHeight, width)
        }
    }

    private const val LOG = "PdfHelper"

}