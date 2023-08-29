package com.lrm.gdgvizag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
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
    private val onItemClicked: (EventRegistration, Event) -> Unit,
) : RecyclerView.Adapter<SubmittedApplicationsAdapter.ApplicationViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class ApplicationViewHolder(
        private val binding: SubmittedApplicationListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            Glide.with(context).load(event.imageUrl).placeholder(R.drawable.loading_icon_anim)
                .into(binding.eventImage)
            binding.eventName.text = event.eventName
            binding.eventType.text = event.eventType
            binding.eventDate.text = event.eventDate
        }

        fun bindApplicationStatus(eventReg: EventRegistration, event: Event) {
            binding.applicationStatus.text = eventReg.applicationStatus
            binding.acceptanceStatus.text = eventReg.acceptanceStatus
            if (eventReg.acceptanceStatus == "Accepted" && eventReg.ticketGenerated == "false") {
                binding.generateTicket.visibility = View.VISIBLE
                binding.generateTicket.setOnClickListener { onItemClicked(eventReg, event) }
            } else binding.generateTicket.visibility = View.GONE

            if (eventReg.ticketGenerated == "true") {
                binding.divider2.visibility = View.VISIBLE
                binding.ticketGeneratedText.visibility = View.VISIBLE
                binding.goToTicketsLl.visibility = View.VISIBLE
            }

            binding.goToTicketsLl.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick()
                }
            }
        }
    }

    interface OnClickListener {
        fun onClick()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        return ApplicationViewHolder(
            SubmittedApplicationListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return submittedApplicationList.size
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        val submittedApplication = submittedApplicationList[position]
        val event = eventsList.single { it.eventId == submittedApplication.eventId }
        holder.bind(event)
        holder.bindApplicationStatus(submittedApplication, event)
    }
}