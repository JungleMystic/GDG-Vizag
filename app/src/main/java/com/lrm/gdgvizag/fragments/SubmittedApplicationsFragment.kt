package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lrm.gdgvizag.adapter.SubmittedApplicationsAdapter
import com.lrm.gdgvizag.databinding.FragmentSubmittedApplicationsBinding
import com.lrm.gdgvizag.viewmodel.AppViewModel
import com.lrm.gdgvizag.viewmodel.ProfileViewModel

class SubmittedApplicationsFragment : Fragment() {

    private var _binding: FragmentSubmittedApplicationsBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubmittedApplicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener { this.findNavController().navigateUp() }

        appViewModel.getSubmittedApplications(profileViewModel.userProfile.value!!.userMail)

        appViewModel.submittedApplicationList.observe(viewLifecycleOwner) {list ->
            if (list.isNotEmpty()) {
                binding.noApplicationsFound.visibility = View.GONE
                binding.submittedRv.visibility = View.VISIBLE
                binding.submittedRv.adapter = SubmittedApplicationsAdapter(
                    requireContext(), list,
                    appViewModel.eventsList.value!!
                ) {
                    Toast.makeText(requireContext(), "Generating Ticket...", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.noApplicationsFound.visibility = View.VISIBLE
                binding.submittedRv.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}