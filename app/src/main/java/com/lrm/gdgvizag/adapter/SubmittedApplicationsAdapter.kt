package com.lrm.gdgvizag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.SubmittedApplicationListItemBinding
import com.lrm.gdgvizag.model.Event
import com.lrm.gdgvizag.model.EventRegistration

class SubmittedApplicationsAdapter(
    private val context: Context,
    private val submittedApplicationList: List<EventRegistration>,
    private val eventsList: List<Event>,
    private val onItemClicked: (EventRegistration) -> Unit
): RecyclerView.Adapter<SubmittedApplicationsAdapter.ApplicationViewHolder>() {

    inner class ApplicationViewHolder(
        private val binding: SubmittedApplicationListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            Glide.with(context).load(event.imageUrl).placeholder(R.drawable.loading_icon_anim).into(binding.eventImage)
            binding.eventName.text = event.eventName
            binding.eventType.text = event.eventType
            binding.eventDate.text = event.eventDate
        }

        fun bindApplicationStatus(eventReg: EventRegistration) {
            binding.applicationStatus.text = eventReg.applicationStatus
            binding.acceptanceStatus.text = eventReg.acceptanceStatus
            binding.generateTicket.isClickable = eventReg.acceptanceStatus == "Accepted"
            if (binding.generateTicket.isClickable && eventReg.isTicketGenerated == "false") {
                binding.generateTicket.setOnClickListener { onItemClicked(eventReg) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        return ApplicationViewHolder(
            SubmittedApplicationListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return submittedApplicationList.size
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        val submittedApplication = submittedApplicationList[position]
        holder.bindApplicationStatus(submittedApplication)
        val event = eventsList.single { it.eventId == submittedApplication.eventId }
        holder.bind(event)
    }
}