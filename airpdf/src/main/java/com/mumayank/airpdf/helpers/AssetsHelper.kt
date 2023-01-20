package com.mumayank.airpdf.helpers

import android.content.res.AssetManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object AssetsHelper {

    suspend fun getInputStreamFromAssets(
        assetManager: AssetManager,
        filename: String
    ): InputStream? {
        return withContext(Dispatchers.IO) {
            try {
                assetManager.open(filename)
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }

    suspend fun getFileFromInputStream(cacheDir: File, inputStream: InputStream): File? {
        return withContext(Dispatchers.IO) {
            var file: File? = null
            try {
                file = File(cacheDir, TEMP)
                FileOutputStream(file).use { output ->
                    val buffer = ByteArray(SIZE)
                    var read: Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            } catch (e: Exception) {
                Log.e(LOG, e.toString())
            } finally {
                inputStream.close()
            }
            file
        }
    }

    private const val LOG = "AssetsHelper"
    private const val TEMP = "temp"
    private const val SIZE = 4 * 1024

}