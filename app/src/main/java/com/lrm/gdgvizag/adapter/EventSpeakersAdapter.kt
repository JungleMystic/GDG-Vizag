package com.lrm.gdgvizag.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.EventSpeakerListItemBinding
import com.lrm.gdgvizag.model.Speaker

class EventSpeakersAdapter(
    private val context: Context,
    private val speakersList: List<Speaker>
): RecyclerView.Adapter<EventSpeakersAdapter.SpeakersViewHolder>() {

    inner class SpeakersViewHolder(
        private val binding: EventSpeakerListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(speaker: Speaker) {

            Glide.with(context).load(speaker.image).placeholder(R.drawable.loading_icon_anim).into(binding.speakerImage)

            binding.speakerName.text = speaker.name
            binding.speakerOrg.text = speaker.organization
            binding.speakerTitle.text = speaker.title

            if (speaker.about == "") {
                binding.aboutMeTitle.visibility = View.GONE
                binding.aboutMeInfo.visibility = View.GONE
            } else {
                binding.aboutMeTitle.visibility = View.VISIBLE
                binding.aboutMeInfo.visibility = View.VISIBLE
                binding.aboutMeInfo.text = speaker.about
            }

            if (speaker.twitterLink == "") {
                binding.twitterXLink.visibility = View.GONE
            } else {
                binding.twitterXLink.visibility = View.VISIBLE
            }

            binding.twitterXLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(speaker.twitterLink))
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakersViewHolder {
        return SpeakersViewHolder(
            EventSpeakerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return speakersList.size
    }

    override fun onBindViewHolder(holder: SpeakersViewHolder, position: Int) {
        val speaker = speakersList[position]
        holder.bind(speaker)
    }
}