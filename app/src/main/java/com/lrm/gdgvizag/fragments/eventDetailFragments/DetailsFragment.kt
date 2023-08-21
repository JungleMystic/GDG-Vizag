package com.lrm.gdgvizag.fragments.eventDetailFragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.FragmentDetailsBinding
import com.lrm.gdgvizag.model.EventDetail
import com.lrm.gdgvizag.viewmodel.AppViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel.eventDetail.observe(viewLifecycleOwner) {event ->
            if (event != null) {
                bind(event)
            }
        }
    }

    private fun bind(event: EventDetail) {
        Glide.with(requireContext()).load(event.eventLogo).placeholder(R.drawable.loading_icon_anim).into(binding.eventImage)
        binding.eventName.text = event.eventName
        binding.eventType.text = event.eventType
        binding.eventDate.text = event.eventDate
        binding.eventWhen.text = event.eventWhen.replace("/n", "\n")
        binding.eventLocation.text = event.eventWhere.replace("/n", "\n")
        binding.aboutEvent.text = event.aboutEvent.replace("/n", "\n")
        binding.mapIcon.setOnClickListener {
            if (event.eventLocation != "") {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.eventLocation))
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}