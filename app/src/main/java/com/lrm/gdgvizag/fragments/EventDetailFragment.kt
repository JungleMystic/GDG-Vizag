package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentEventDetailBinding
import com.lrm.gdgvizag.viewmodel.AppViewModel

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()
    private val navArgs: EventDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = navArgs.eventId
        Log.i(TAG, "EventDetail onViewCreated: eventId -> $eventId")
        appViewModel.getEventDetailedData(eventId, requireActivity())

        appViewModel.eventDetail.observe(viewLifecycleOwner) {data ->

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}