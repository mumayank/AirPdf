package com.example.webviewwithpdf

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.webviewwithpdf.databinding.ActivityHomeBinding
import com.example.webviewwithpdf.pdf_in_rv.PdfInRvActivity
import com.mumayank.airpdf.helpers.PdfHelper

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        findViewById<Button>(R.id.rvButton).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PdfInRvActivity::class.java
                ).putExtra(
                    PdfInRvActivity.INTENT_EXTRA,
                    true
                )
            )
        }

        findViewById<Button>(R.id.rvButton2).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PdfInRvActivity::class.java
                ).putExtra(
                    PdfInRvActivity.INTENT_EXTRA,
                    false
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        PdfHelper.deleteBitmaps(cacheDir, (application as App).getBitmapFilenames())
        (application as App).updateBitmapFilenames()
    }
}