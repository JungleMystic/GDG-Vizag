package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.carousel.CarouselLayoutManager
import com.lrm.gdgvizag.adapter.MemoriesAdapter
import com.lrm.gdgvizag.adapter.UpcomingEventAdapter
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentHomeBinding
import com.lrm.gdgvizag.viewmodel.AppViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel.getImages()
        appViewModel.getEventsData()
        appViewModel.imagesList.observe(viewLifecycleOwner) {list ->
            //Log.i(TAG, "Home Fragment imagesList-> $list")
            binding.imageRv.apply {
                layoutManager = CarouselLayoutManager()
                adapter = MemoriesAdapter(requireContext(), list)
            }
        }

        appViewModel.upcomingEventsList.observe(viewLifecycleOwner) {list ->
            Log.i(TAG, "Home Fragment upcomingEventList-> $list")
            if (list != null) {
                binding.upcomingRv.visibility = View.VISIBLE
                binding.noUpcomingEvents.visibility = View.INVISIBLE
                binding.upcomingRv.adapter = UpcomingEventAdapter(requireContext(), list)
            } else {
                binding.upcomingRv.visibility = View.INVISIBLE
                binding.noUpcomingEvents.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}