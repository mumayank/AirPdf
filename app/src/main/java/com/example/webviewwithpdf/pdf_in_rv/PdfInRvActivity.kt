package com.example.webviewwithpdf.pdf_in_rv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.webviewwithpdf.databinding.ActivityPdfInRvBinding
import com.mumayank.airpdf.helpers.PdfHelper

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
                    viewModel.getBitmapFilenames(cacheDir, MCB, width)
                    //viewModel.getBitmapFilenames(cacheDir, assets, "sample.pdf", width)
                }
                viewModel.bitmapFilenames.observe(this@PdfInRvActivity) {
                    if (it.isEmpty()) {
                        return@observe
                    }
                    adapter = PdfInRv(this@PdfInRvActivity, cacheDir, it)
                    progressLayout.visibility = View.GONE
                }
            }
            progressLayout.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        viewModel.cleanup(cacheDir)
        super.onDestroy()
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