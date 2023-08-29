package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lrm.gdgvizag.adapter.YourTicketsAdapter
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentYourTicketsBinding
import com.lrm.gdgvizag.viewmodel.AppViewModel
import com.lrm.gdgvizag.viewmodel.ProfileViewModel

class YourTicketsFragment : Fragment() {

    private var _binding: FragmentYourTicketsBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYourTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener { this.findNavController().navigateUp() }

        val userMail = profileViewModel.userProfile.value!!.userMail
        appViewModel.getTickets(userMail)

        binding.swipeRefreshLayout.setOnRefreshListener {
            appViewModel.getTickets(userMail)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        appViewModel.yourTicketsList.observe(viewLifecycleOwner) {list ->
            if (list.isNotEmpty()) {
                Log.i(TAG, "onViewCreated: Your Tickets -> $list")
                binding.noTicketsFound.visibility = View.GONE
                binding.ticketsRv.visibility = View.VISIBLE
                binding.ticketsRv.adapter = YourTicketsAdapter(requireContext(), list, appViewModel.eventsList.value!!)
            } else {
                binding.noTicketsFound.visibility = View.VISIBLE
                binding.ticketsRv.visibility = View.INVISIBLE
            }
        }

        binding.qrCodeIcon.setOnClickListener {
            val action = YourTicketsFragmentDirections.actionYourTicketsFragmentToScanQrCodeFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}