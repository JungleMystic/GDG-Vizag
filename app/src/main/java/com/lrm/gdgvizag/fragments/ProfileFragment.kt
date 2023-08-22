package com.lrm.gdgvizag.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentProfileBinding
import com.lrm.gdgvizag.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.userProfile.observe(viewLifecycleOwner) {user ->
            if (user != null) {
                binding.profileName.text = user.userName
                binding.profileMail.text = user.userMail
            }
        }

        binding.logOutCard.setOnClickListener {
            showLogOutDialog()
        }

        binding.editProfileCard.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun showLogOutDialog() {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.custom_log_out_dialog, null)
        val yesTv = dialogView.findViewById<TextView>(R.id.yes_tv)
        val noTv = dialogView.findViewById<TextView>(R.id.no_tv)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        builder.setCancelable(true)

        val logoutDialog = builder.create()
        logoutDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.show()

        yesTv.setOnClickListener {
            logOut()
            logoutDialog.dismiss()
        }

        noTv.setOnClickListener {
            logoutDialog.dismiss()
        }
    }

    private fun logOut() {
        profileViewModel.setUserProfile(null)
        Log.i(TAG, "ProfileFragment: After SignOut ${profileViewModel.userProfile.value}")
        auth.signOut()
        val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}