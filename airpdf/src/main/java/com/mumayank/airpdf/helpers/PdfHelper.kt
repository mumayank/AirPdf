package com.mumayank.airpdf.helpers

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object PdfHelper {

    suspend fun getBitmapFilenames(
        dir: File,
        assetManager: AssetManager,
        assetFilename: String,
        width: Int
    ): List<String>? {
        val filename =
            AssetsHelper.openPdfFromAssetAndGetFilename(dir, assetManager, assetFilename)
                ?: return null
        return getBitmapFilenamesFromPdf(dir, filename, width)
    }

    suspend fun getBitmapFilenames(
        dir: File,
        url: String,
        width: Int
    ): List<String>? {
        val pdfFilename =
            FileDownloadHelper.downloadPdfFromUrlWriteToFileAndGetFilename(dir, url) ?: return null
        return getBitmapFilenamesFromPdf(dir, pdfFilename, width)
    }

    private suspend fun getBitmapFilenamesFromPdf(
        dir: File,
        pdfFilename: String,
        width: Int
    ): List<String>? {
        return withContext(Dispatchers.IO) {
            val bitmapFilenames = arrayListOf<String>()
            var index = 0
            while (true) {
                try {
                    val bitmap = getBitmap(dir, pdfFilename, width, index++) ?: break
                    val fileName = System.currentTimeMillis().toString()
                    val bitmapFile = File.createTempFile(fileName, null, dir)
                    val os: OutputStream = BufferedOutputStream(FileOutputStream(bitmapFile))
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                    os.close()
                    bitmapFilenames.add(bitmapFile.name)
                } catch (e: java.lang.Exception) {
                    break
                }
            }
            File(dir, pdfFilename).delete()
            if (bitmapFilenames.isEmpty()) {
                return@withContext null
            }
            bitmapFilenames
        }
    }

    private suspend fun getBitmap(
        cacheDir: File,
        filename: String,
        width: Int,
        index: Int
    ): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(cacheDir, filename)
                val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val renderer = PdfRenderer(pfd)
                val bitmap = Bitmap.createBitmap(
                    width,
                    getA4Height(width),
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

    private suspend fun getA4Height(width: Int): Int {
        return withContext(Dispatchers.Default) {
            val a4ratio = 1.35
            val imageViewA4Height = width * a4ratio
            imageViewA4Height.toInt()
        }
    }

    fun deleteBitmaps(dir: File, bitmapFilenames: List<String>) {
        for (bitmapFilename in bitmapFilenames) {
            File(dir, bitmapFilename).delete()
        }
    }

    private const val LOG = "PdfHelper"

}