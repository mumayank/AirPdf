package com.example.webviewwithpdf.pdf_as_bitmap

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.webviewwithpdf.databinding.ActivityPdfAsBitmapBinding

class PdfAsBitmapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfAsBitmapBinding
    private val viewModel by viewModels<PdfAsBitmapViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfAsBitmapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        with(binding) {
            acceptButton.setOnClickListener {
                showToastAndCleanupAndExit(true)
            }
            rejectButton.setOnClickListener {
                showToastAndCleanupAndExit(false)
            }
            previousButton.setOnClickListener {
                progressLayout.visibility = View.VISIBLE
                viewModel.onPreviousPage()
            }
            nextButton.setOnClickListener {
                progressLayout.visibility = View.VISIBLE
                viewModel.onNextPage()
            }

            with(zoomageView) {
                post {
                    viewModel.setViewMeasurements(width)
                }
            }

            viewModel.currentIndex.observe(this@PdfAsBitmapActivity) {
                updateIndexTextView()
                when {
                    (it < 1) -> {
                        previousButton.isEnabled = false
                        nextButton.isEnabled = true
                    }

                    (it == viewModel.lastIndex.value) -> {
                        previousButton.isEnabled = true
                        nextButton.isEnabled = false
                    }

                    else -> {
                        previousButton.isEnabled = true
                        nextButton.isEnabled = true
                    }
                }
            }
            viewModel.bitmap.observe(this@PdfAsBitmapActivity) {
                runOnUiThread {
                    zoomageView.setImageBitmap(it)
                    progressLayout.visibility = View.GONE
                }
            }
            viewModel.lastIndex.observe(this@PdfAsBitmapActivity) {
                updateIndexTextView()
            }
            viewModel.error.observe(this@PdfAsBitmapActivity) {
                if (it) {
                    AlertDialog.Builder(this@PdfAsBitmapActivity)
                        .setTitle("Something went wrong")
                        .setPositiveButton("Retry") { _, _ ->
                            loadFirstPage()
                            viewModel.resetError()
                        }
                        .setNegativeButton("Exit") { _, _ ->
                            finish()
                        }
                }
            }
        }

        loadFirstPage()
    }

    private fun loadFirstPage() {
        when (intent.getStringExtra(INTENT_EXTRA)) {
            Type.Image.name -> viewModel.loadFirstPdfPage(cacheDir, SEMI_LARGE_IMAGE_PDF_URL)
            Type.Large.name -> viewModel.loadFirstPdfPage(cacheDir, LARGE_PDF_URL)
            else -> viewModel.loadFirstPdfPage(
                cacheDir, assets, intent.getStringExtra(
                    INTENT_EXTRA_ASSET_FILENAME
                ).toString()
            )
        }
    }

    private fun updateIndexTextView() {
        val currentIndex = (viewModel.currentIndex.value ?: 0) + 1
        val lastIndex = (viewModel.lastIndex.value ?: 0) + 1
        binding.indexTextView.text = "$currentIndex / $lastIndex"
    }

    private fun showToastAndCleanupAndExit(isTncAccepted: Boolean) {
        Toast.makeText(
            this@PdfAsBitmapActivity,
            if (isTncAccepted) "TnC Accepted" else "TnC Rejected",
            Toast.LENGTH_SHORT
        ).show()
        viewModel.deleteDownloadedFiles(this@PdfAsBitmapActivity.cacheDir)
        finish()
    }

    companion object {
        const val INTENT_EXTRA = "INTENT_EXTRA"
        const val INTENT_EXTRA_ASSET_FILENAME = "INTENT_EXTRA_ASSET_FILENAME"
        private const val LARGE_PDF_URL = "https://research.nhm.org/pdfs/10840/10840.pdf"
        private const val SEMI_LARGE_IMAGE_PDF_URL =
            "https://research.nhm.org/pdfs/10840/10840-002.pdf"
    }

    enum class Type {
        Assets,
        Image,
        Large
    }
}