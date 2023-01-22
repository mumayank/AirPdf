package com.example.webviewwithpdf.pdf_in_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.webviewwithpdf.databinding.LayoutPdfInRvBinding
import java.io.File

class PdfInRv(
    private val context: Context,
    private val dir: File,
    private val bitmapFilename: List<String>
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RvViewHolder(
        LayoutPdfInRvBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bitmapFilename[position].let {
            with((holder as RvViewHolder).binding) {
                Glide.with(context)
                    .load(File(dir, it))
                    .into(zoomableImageView)
            }
        }
    }

    override fun getItemCount() = bitmapFilename.size

    inner class RvViewHolder(val binding: LayoutPdfInRvBinding) : ViewHolder(binding.root)
}