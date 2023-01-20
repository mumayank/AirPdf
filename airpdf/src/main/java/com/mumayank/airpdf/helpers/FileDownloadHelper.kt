package com.mumayank.airpdf.helpers

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object FileDownloadHelper {

    suspend fun downloadFromUrlAndGetName(
        cacheDir: File,
        url: String
    ): String? {
        return try {
            withContext(Dispatchers.IO) {
                val fileName = System.currentTimeMillis().toString()
                val pdfFile = File.createTempFile(fileName, null, cacheDir)
                val urls = URL(url)
                val urlConnection = urls.openConnection() as HttpURLConnection
                urlConnection.connect()
                val inputStream = urlConnection.inputStream
                val fileOutputStream = FileOutputStream(pdfFile)
                val buffer = ByteArray(SIZE)
                var bufferLength = 0
                while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength)
                }
                fileOutputStream.close()
                pdfFile.name
            }
        } catch (e: java.lang.Exception) {
            Log.e(LOG, e.toString())
            null
        }
    }

    suspend fun deleteAllDownloadedFiles(
        cacheDir: File
    ) {
        withContext(Dispatchers.IO) {
            cacheDir.deleteRecursively()
        }
    }

    private const val LOG = "FileDownloadHelper"
    private const val SIZE = 4 * 1024
}