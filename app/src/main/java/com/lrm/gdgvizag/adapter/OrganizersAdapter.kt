package com.lrm.gdgvizag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.OrganizerListItemBinding
import com.lrm.gdgvizag.model.Organizer

class OrganizersAdapter(
    private val context: Context,
    private val organizersList: List<Organizer>,
    private val onItemClicked: (Organizer) -> Unit
): RecyclerView.Adapter<OrganizersAdapter.UpcomingViewHolder>() {

    inner class UpcomingViewHolder(
        private val binding: OrganizerListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(organizer: Organizer) {
            Glide.with(context).load(organizer.imageUrl).placeholder(R.drawable.loading_icon_anim).into(binding.organizerImage)
            binding.organizerName.text = organizer.name
            binding.orgName.text = organizer.orgName
            binding.organizerTitle.text = organizer.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        return UpcomingViewHolder(
            OrganizerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return organizersList.size
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        val organizer = organizersList[position]
        holder.bind(organizer)
        holder.itemView.setOnClickListener { onItemClicked(organizer) }
    }
}