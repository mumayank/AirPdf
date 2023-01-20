package com.example.webviewwithpdf

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.webviewwithpdf.databinding.ActivityHomeBinding
import com.example.webviewwithpdf.pdf_as_bitmap.PdfAsBitmapActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        findViewById<Button>(R.id.pdfFromAssetsButton).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PdfAsBitmapActivity::class.java
                ).putExtra(
                    PdfAsBitmapActivity.INTENT_EXTRA,
                    PdfAsBitmapActivity.Type.Assets.name
                ).putExtra(
                    PdfAsBitmapActivity.INTENT_EXTRA_ASSET_FILENAME,
                    ASSETS_PDF_FILENAME
                )
            )
        }

        findViewById<Button>(R.id.imagePdfButton).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PdfAsBitmapActivity::class.java
                ).putExtra(
                    PdfAsBitmapActivity.INTENT_EXTRA,
                    PdfAsBitmapActivity.Type.Image.name
                )
            )
        }

        findViewById<Button>(R.id.largePdfButton).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PdfAsBitmapActivity::class.java
                ).putExtra(
                    PdfAsBitmapActivity.INTENT_EXTRA,
                    PdfAsBitmapActivity.Type.Large.name
                )
            )
        }
    }

    companion object {
        private const val ASSETS_PDF_FILENAME = "sample.pdf"
    }
}