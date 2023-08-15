package com.lrm.gdgvizag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.ImageListItemBinding

class MemoriesAdapter(
    private val context: Context,
    private val imagesList: List<String>
): RecyclerView.Adapter<MemoriesAdapter.ImageViewHolder>() {

    class ImageViewHolder(binding: ImageListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val image = binding.carouselImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ImageListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imagesList[position]
        Glide.with(context).load(image).placeholder(R.drawable.loading_icon_anim).centerCrop().into(holder.image)
    }
}