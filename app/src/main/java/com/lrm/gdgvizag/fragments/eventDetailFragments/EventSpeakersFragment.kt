package com.lrm.gdgvizag.fragments.eventDetailFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lrm.gdgvizag.adapter.EventSpeakersAdapter
import com.lrm.gdgvizag.databinding.FragmentEventSpeakersBinding
import com.lrm.gdgvizag.viewmodel.AppViewModel

class EventSpeakersFragment : Fragment() {

    private var _binding: FragmentEventSpeakersBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventSpeakersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel.eventDetail.observe(viewLifecycleOwner) {event ->
            if (event != null) {
                binding.eventSpeakersRv.adapter = EventSpeakersAdapter(requireContext(), event.speakersList)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}