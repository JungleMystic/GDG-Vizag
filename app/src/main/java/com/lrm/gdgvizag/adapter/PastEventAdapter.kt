package com.lrm.gdgvizag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.EventListItemBinding
import com.lrm.gdgvizag.model.Event

class PastEventAdapter(
    private val context: Context,
    private val pastEventList: List<Event>,
    private val onItemClicked: (Event) -> Unit
): RecyclerView.Adapter<PastEventAdapter.UpcomingViewHolder>() {

    inner class UpcomingViewHolder(
        private val binding: EventListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            Glide.with(context).load(event.imageUrl).placeholder(R.drawable.loading_icon_anim).into(binding.eventImage)
            binding.eventName.text = event.eventName
            binding.eventType.text = event.eventType
            binding.eventDate.text = event.eventDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        return UpcomingViewHolder(
            EventListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return pastEventList.size
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        val event = pastEventList[position]
        holder.bind(event)
        holder.itemView.setOnClickListener { onItemClicked(event) }
    }
}