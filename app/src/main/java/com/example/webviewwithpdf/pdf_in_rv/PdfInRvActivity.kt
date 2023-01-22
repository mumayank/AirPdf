package com.example.webviewwithpdf.pdf_in_rv

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.webviewwithpdf.App
import com.example.webviewwithpdf.databinding.ActivityPdfInRvBinding

class PdfInRvActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfInRvBinding
    private val viewModel by viewModels<PdfInRvViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfInRvBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        with(binding) {
            with(rv) {
                post {
                    if (intent.getBooleanExtra(INTENT_EXTRA, false)) {
                        viewModel.updateBitmapFilename(cacheDir, assets, "story.pdf", width)
                    } else {
                        viewModel.updateBitmapFilename(cacheDir, SEMI_LARGE_IMAGE_PDF_URL, width)
                    }
                }
                viewModel.bitmapFilenames.observe(this@PdfInRvActivity) {
                    if (it.isEmpty()) {
                        return@observe
                    }
                    (application as App).updateBitmapFilenames(it)
                    adapter = PdfInRv(this@PdfInRvActivity, cacheDir, it)
                    progressLayout.visibility = View.GONE
                }
            }
            progressLayout.visibility = View.VISIBLE
        }
    }

    companion object {
        const val INTENT_EXTRA = "INTENT_EXTRA"
        const val INTENT_EXTRA_ASSET_FILENAME = "INTENT_EXTRA_ASSET_FILENAME"
        private const val LARGE_PDF_URL = "https://research.nhm.org/pdfs/10840/10840.pdf"
        private const val SEMI_LARGE_IMAGE_PDF_URL =
            "https://research.nhm.org/pdfs/10840/10840-002.pdf"
        private const val MCB =
            "https://gateway-backbase-mu-qa.mcbengineering.info/api/contentservices/api/contentstream/smart-approve/terms-and-conditions/SmartApprove_Terms_and_Conditions.pdf"
    }
}