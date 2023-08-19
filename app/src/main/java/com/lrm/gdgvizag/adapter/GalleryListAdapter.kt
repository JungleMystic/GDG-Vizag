package com.lrm.gdgvizag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.GalleryListItemBinding

class GalleryListAdapter(
    private val context: Context,
    private val galleryList: List<String>
): RecyclerView.Adapter<GalleryListAdapter.GalleryViewHolder>() {
    inner class GalleryViewHolder(private val binding: GalleryListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        val galleryItem = binding.galleryIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            GalleryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return galleryList.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val image = galleryList[position]
        Glide.with(context).load(image).placeholder(R.drawable.loading_icon_anim).into(holder.galleryItem)
    }
}