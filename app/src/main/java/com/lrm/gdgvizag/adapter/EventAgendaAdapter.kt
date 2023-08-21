package com.lrm.gdgvizag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lrm.gdgvizag.databinding.AgendaListItemBinding

class EventAgendaAdapter(
    private val agendaMap: Map<String, List<String>>
): RecyclerView.Adapter<EventAgendaAdapter.AgendaViewHolder>() {

    inner class AgendaViewHolder(
        private val binding: AgendaListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(topicData: List<String>) {
            binding.topicTime.text = topicData[0]
            binding.topicName.text = topicData[1]
            binding.topicSpeaker.text = topicData[2]
            if (topicData[2] == "") {
                binding.topicSpeaker.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder {
        return AgendaViewHolder(
            AgendaListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return agendaMap.size
    }

    override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) {
        val topicData = agendaMap["$position"]!!
        holder.bind(topicData)
    }
}