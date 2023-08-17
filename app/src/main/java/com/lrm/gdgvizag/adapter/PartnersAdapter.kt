package com.lrm.gdgvizag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.PartnerListItemBinding
import com.lrm.gdgvizag.model.Partner

class PartnersAdapter(
    private val context: Context,
    private val partnersList: List<Partner>,
    private val onItemClicked: (Partner) -> Unit
): RecyclerView.Adapter<PartnersAdapter.UpcomingViewHolder>() {

    inner class UpcomingViewHolder(
        private val binding: PartnerListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(partner: Partner) {
            Glide.with(context).load(partner.logo).placeholder(R.drawable.loading_icon_anim).into(binding.partnerImage)
            binding.partnerName.text = partner.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        return UpcomingViewHolder(
            PartnerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return partnersList.size
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        val partner = partnersList[position]
        holder.bind(partner)
        holder.itemView.setOnClickListener { onItemClicked(partner) }
    }
}