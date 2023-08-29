package com.lrm.gdgvizag.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.TicketsListItemBinding
import com.lrm.gdgvizag.model.Event
import com.lrm.gdgvizag.model.QrCode

class YourTicketsAdapter(
    private val context: Context,
    private val yourTicketsList: List<QrCode>,
    private val eventsList: List<Event>,
) : RecyclerView.Adapter<YourTicketsAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(
        private val binding: TicketsListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event, ticket: QrCode) {
            Glide.with(context).load(event.imageUrl).placeholder(R.drawable.loading_icon_anim)
                .into(binding.eventImage)
            binding.eventName.text = event.eventName
            binding.eventType.text = event.eventType
            binding.eventDate.text = event.eventDate

            binding.showTicket.setOnClickListener {
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.qr_code_image_view)
                val qrCodeImage = dialog.findViewById<ImageView>(R.id.qr_code_image_d)
                Glide.with(context).load(ticket.qrCodeImageLink).placeholder(R.drawable.loading_icon_anim).into(qrCodeImage)
                dialog.setCancelable(true)
                dialog.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        return TicketViewHolder(
            TicketsListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return yourTicketsList.size
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = yourTicketsList[position]
        val event = eventsList.single { it.eventId == ticket.eventId }
        holder.bind(event, ticket)
    }
}