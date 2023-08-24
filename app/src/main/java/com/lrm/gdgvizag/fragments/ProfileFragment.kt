package com.lrm.gdgvizag.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentProfileBinding
import com.lrm.gdgvizag.model.AboutGDG
import com.lrm.gdgvizag.utils.AboutGDGDialog
import com.lrm.gdgvizag.viewmodel.ProfileViewModel
import de.hdodenhof.circleimageview.CircleImageView

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

        binding.backIcon.setOnClickListener { this.findNavController().navigateUp() }

        profileViewModel.userProfile.observe(viewLifecycleOwner) {user ->
            if (user != null) {
                binding.profileName.text = user.userName
                binding.profileMail.text = user.userMail
                if (user.userPic == "") {
                    binding.profilePic.setImageResource(R.drawable.profile_user_icon)
                } else {
                    Glide.with(this@ProfileFragment)
                        .load(user.userPic)
                        .placeholder(R.drawable.profile_user_icon)
                        .into(binding.profilePic)
                }
            }
        }

        binding.logOutCard.setOnClickListener {
            showLogOutDialog()
        }

        binding.editProfileCard.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            this.findNavController().navigate(action)
        }

        binding.organizersCard.setOnClickListener {
            val modalBottomSheet = ProfileBottomSheetFragment("organizers")
            modalBottomSheet.show(requireActivity().supportFragmentManager, ProfileBottomSheetFragment.TAG)
        }

        binding.partnersCard.setOnClickListener {
            val modalBottomSheet = ProfileBottomSheetFragment("partners")
            modalBottomSheet.show(requireActivity().supportFragmentManager, ProfileBottomSheetFragment.TAG)
        }

        binding.gdgVizagCard.setOnClickListener {
            val infoDialog = AboutGDGDialog(requireActivity(), requireContext())
            infoDialog.showInfo(AboutGDG())
        }

        binding.appVersionCard.setOnClickListener {
            Toast.makeText(requireContext(), "Long press on App version to get Developer Info...", Toast.LENGTH_SHORT).show()
        }

        binding.appVersionCard.setOnLongClickListener {
            showDeveloperInfoDialog()
            true
        }
    }

    private fun showDeveloperInfoDialog() {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.custom_developer_info, null)
        val imageLink = "https://firebasestorage.googleapis.com/v0/b/gdg-vizag-f9bf0.appspot.com/o/gdg_vizag%2Fdeveloper%2FRammohan_L_pic.png?alt=media&token=6e55ba28-e0ca-45c6-b50b-be1955da2566"
        val devImage = dialogView.findViewById<CircleImageView>(R.id.dev_image)
        Glide.with(requireContext()).load(imageLink).placeholder(R.drawable.loading_icon_anim).into(devImage)

        val devGithubLink = dialogView.findViewById<CircleImageView>(R.id.dev_github_link)
        val devYoutubeLink = dialogView.findViewById<CircleImageView>(R.id.dev_youtube_link)

        devGithubLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JungleMystic"))
            startActivity(intent)
        }

        devYoutubeLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/@junglemystic"))
            startActivity(intent)
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        builder.setCancelable(true)

        val logoutDialog = builder.create()
        logoutDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.show()
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