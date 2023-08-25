package com.lrm.gdgvizag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.EventListItemBinding
import com.lrm.gdgvizag.model.Event

class UpcomingEventAdapter(
    private val context: Context,
    private val upcomingEventList: List<Event>,
    private val onItemClicked: (Event) -> Unit
): RecyclerView.Adapter<UpcomingEventAdapter.UpcomingViewHolder>() {

    inner class UpcomingViewHolder(
        private val binding: EventListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            Glide.with(context).load(event.imageUrl).placeholder(R.drawable.loading_icon_anim).into(binding.eventImage)
            binding.eventName.text = event.eventName
            binding.eventType.text = event.eventType
            binding.eventDate.text = event.eventDate
            binding.eventRegistration.text = event.registrationText
            if (event.registrationStatus == "open") {
                binding.registerButton.visibility = View.VISIBLE
            } else {
                binding.registerButton.visibility = View.INVISIBLE
            }

            binding.registerButton.setOnClickListener{onItemClicked(event)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        return UpcomingViewHolder(
            EventListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return upcomingEventList.size
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        val event = upcomingEventList[position]
        holder.bind(event)
    }
}