package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.carousel.CarouselLayoutManager
import com.lrm.gdgvizag.adapter.MemoriesAdapter
import com.lrm.gdgvizag.adapter.OrganizersAdapter
import com.lrm.gdgvizag.adapter.PartnersAdapter
import com.lrm.gdgvizag.adapter.PastEventAdapter
import com.lrm.gdgvizag.adapter.UpcomingEventAdapter
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentHomeBinding
import com.lrm.gdgvizag.utils.OrganizerInfoDialog
import com.lrm.gdgvizag.utils.PartnerInfoDialog
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
        appViewModel.getPartnersData()
        appViewModel.getOrganizersData()

        appViewModel.imagesList.observe(viewLifecycleOwner) {list ->
            //Log.i(TAG, "Home Fragment imagesList-> $list")
            binding.imageRv.apply {
                layoutManager = CarouselLayoutManager()
                adapter = MemoriesAdapter(requireContext(), list)
            }
        }

        appViewModel.upcomingEventsList.observe(viewLifecycleOwner) {list ->
            Log.i(TAG, "Home Fragment upcomingEventList-> $list")
            if (list.isNotEmpty()) {
                binding.upcomingRv.visibility = View.VISIBLE
                binding.noUpcomingEvents.visibility = View.INVISIBLE
                binding.upcomingRv.adapter = UpcomingEventAdapter(requireContext(), list)
            } else {
                binding.upcomingRv.visibility = View.INVISIBLE
                binding.noUpcomingEvents.visibility = View.VISIBLE
            }
        }

        appViewModel.pastEventsList.observe(viewLifecycleOwner) {list ->
            Log.i(TAG, "Home Fragment pastEventList-> $list")
            if (list.isNotEmpty()) {
                binding.pastRv.visibility = View.VISIBLE
                binding.noPastEvents.visibility = View.INVISIBLE
                binding.pastRv.adapter = PastEventAdapter(requireContext(), list)
            } else {
                binding.pastRv.visibility = View.INVISIBLE
                binding.noPastEvents.visibility = View.VISIBLE
            }
        }

        appViewModel.partnersList.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                binding.partnersRv.adapter = PartnersAdapter(requireContext(), list) {
                    val infoDialog = PartnerInfoDialog(requireActivity(), requireContext())
                    infoDialog.showInfo(it)
                }
            }
        }

        appViewModel.organizersList.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                binding.organizersRv.adapter = OrganizersAdapter(requireContext(), list) {
                    val infoDialog = OrganizerInfoDialog(requireActivity(), requireContext())
                    infoDialog.showInfo(it)
                }
            }
        }

        binding.profileIcon.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}