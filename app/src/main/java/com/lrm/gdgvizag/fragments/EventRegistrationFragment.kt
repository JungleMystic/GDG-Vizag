package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.FragmentEventRegistrationBinding
import com.lrm.gdgvizag.model.Event
import com.lrm.gdgvizag.model.User
import com.lrm.gdgvizag.viewmodel.ProfileViewModel

class EventRegistrationFragment : Fragment() {

    private var _binding: FragmentEventRegistrationBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val eventRegArgs: EventRegistrationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = eventRegArgs.event
        bindEventDetails(event)

        profileViewModel.userProfile.observe(viewLifecycleOwner) {user ->
            if (user != null) {
                bind(user)
            }
        }
    }

    private fun bindEventDetails(event: Event) {
        binding.apply {
            eventName.text = event.eventName
            eventDate.text = event.eventDate
            eventType.text = event.eventType
            Glide.with(requireContext()).load(event.imageUrl).placeholder(R.drawable.loading_icon_anim).into(eventImage)
        }
    }

    private fun bind(user: User) {
        binding.profileMail.text = user.userMail
        binding.profileName.setText(user.userName, TextView.BufferType.SPANNABLE)
        binding.mobileNumber.setText(user.userPhoneNum, TextView.BufferType.SPANNABLE)
        binding.genderActv.setText(user.userGender)
    }

    override fun onResume() {
        super.onResume()
        val genders = resources.getStringArray(R.array.gender_array)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_tv, genders)
        binding.genderActv.setAdapter(arrayAdapter)

        val csArray = resources.getStringArray(R.array.cs_array)
        val arrayAdapter2 = ArrayAdapter(requireContext(), R.layout.drop_down_tv, csArray)
        binding.csActv.setAdapter(arrayAdapter2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}