package com.mumayank.airpdf.helpers

import android.content.res.AssetManager
import android.renderscript.ScriptGroup.Input
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object AssetsHelper {

    internal suspend fun openPdfFromAssetAndGetFilename(
        dir: File,
        assetManager: AssetManager,
        assetFilename: String
    ): String? {
        return withContext(Dispatchers.IO) {
            var file: File? = null
            var inputStream: InputStream? = null
            try {
                inputStream = assetManager.open(assetFilename)
                file = File(dir, System.currentTimeMillis().toString())
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
                inputStream?.close()
            }
            file?.name
        }
    }

    private const val LOG = "AssetsHelper"
    private const val SIZE = 4 * 1024

}