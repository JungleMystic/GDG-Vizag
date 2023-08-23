package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.FragmentEditProfileBinding
import com.lrm.gdgvizag.model.User
import com.lrm.gdgvizag.viewmodel.ProfileViewModel

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.userProfile.observe(viewLifecycleOwner) {user ->
            if (user != null) {
                bind(user)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val genders = resources.getStringArray(R.array.gender_array)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_tv, genders)
        binding.genderActv.setAdapter(arrayAdapter)
    }

    private fun bind(user: User) {
        binding.profileName.setText(user.userName, TextView.BufferType.SPANNABLE)
        binding.profileMail.setText(user.userMail, TextView.BufferType.SPANNABLE)
        if (user.userPhoneNum != "") {
            binding.mobileNumber.setText(user.userPhoneNum, TextView.BufferType.SPANNABLE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}