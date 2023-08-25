package com.lrm.gdgvizag.fragments

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.constants.PermissionCodes
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.constants.USERS
import com.lrm.gdgvizag.databinding.FragmentEditProfileBinding
import com.lrm.gdgvizag.model.User
import com.lrm.gdgvizag.utils.LoadingDialog
import com.lrm.gdgvizag.viewmodel.ProfileViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog
    private var filePath: Uri = Uri.EMPTY
    private lateinit var storageRef: StorageReference

    private val contract = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            filePath = it
            Log.i(TAG, "EditProfileFragment: File path-> $filePath")
            binding.profileImage.setImageURI(filePath)
        } else {
            filePath = Uri.EMPTY
            binding.profileImage.setImageResource(R.drawable.profile_user_icon)
        }
    }

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

        binding.backIcon.setOnClickListener { this.findNavController().navigateUp() }

        profileViewModel.userProfile.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                bind(user)
            }
        }

        binding.profileImage.setOnClickListener {
            if (hasPermission()) {
                pickImageFromGallery()
            } else {
                requestPermission()
            }
        }

        binding.updateButton.setOnClickListener {
            updateDataToFirestore()
        }
    }

    private fun updateDataToFirestore() {

        Log.i(TAG, "updateDataToFirestore is called")

        loadingDialog = LoadingDialog(requireActivity())

        val profileName = binding.profileName.text.toString()
        val profileMail = binding.profileMail.text.toString()
        val profileMobile = binding.mobileNumber.text.toString()
        var profileGender = binding.genderActv.text.toString()
        var profilePic = ""

        if (profileGender == "Gender") {
            profileGender = ""
        }

        storageRef = FirebaseStorage.getInstance().reference

        if (profileViewModel.checkDataToUpdate(profileName, profileMail, profileMobile,
                profileGender)) {

            loadingDialog.startLoading()
            if (filePath != Uri.EMPTY) {
                val reference = storageRef.child("gdg_vizag/profilePics/$profileMail")
                reference.putFile(filePath)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            reference.downloadUrl.addOnSuccessListener { task ->
                                profilePic = task.toString()
                                uploadDataToFirestore(
                                    profileName,
                                    profileMail,
                                    profileMobile,
                                    profileGender,
                                    profilePic
                                )
                            }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Image Upload failed", Toast.LENGTH_SHORT)
                            .show()
                        profilePic = ""
                        uploadDataToFirestore(
                            profileName,
                            profileMail,
                            profileMobile,
                            profileGender,
                            profilePic
                        )
                    }
            } else if (filePath == Uri.EMPTY) {

                profilePic = if (profileViewModel.userProfile.value?.userPic == "") {
                    ""
                } else {
                    profileViewModel.userProfile.value?.userPic.toString()
                }

                uploadDataToFirestore(
                    profileName,
                    profileMail,
                    profileMobile,
                    profileGender,
                    profilePic
                )
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill all the fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun uploadDataToFirestore(
        profileName: String,
        profileMail: String,
        profileMobile: String,
        profileGender: String,
        profilePic: String
    ) {

        Log.i(TAG, "uploadDataToFirestore is called")

        if (profileViewModel.checkDataToUpdate(
                profileName,
                profileMail,
                profileMobile,
                profileGender
            )
        ) {
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            val db: FirebaseFirestore = Firebase.firestore

            lifecycleScope.launch {
                val user = User(
                    profileName,
                    profileMail,
                    profileMobile,
                    auth.currentUser?.uid!!,
                    profileGender,
                    profilePic,
                    "updated"
                )
                db.collection(USERS).document(profileMail).set(user)
                    .addOnSuccessListener {
                        loadingDialog.dismissDialog()
                        Toast.makeText(
                            requireContext(),
                            "Profile updated successfully...",
                            Toast.LENGTH_SHORT
                        ).show()
                        profileViewModel.getUserProfile()
                        this@EditProfileFragment.findNavController().navigateUp()
                    }
                    .addOnFailureListener {
                        loadingDialog.dismissDialog()
                        Toast.makeText(
                            requireContext(),
                            "An error occurred while updating profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        } else {
            loadingDialog.dismissDialog()
            Toast.makeText(
                requireContext(),
                "Please fill all the fields",
                Toast.LENGTH_SHORT
            ).show()
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

        if (user.userGender != "") {
            binding.genderActv.setText(user.userGender)
        }

        if (user.userPic == "") {
            binding.profileImage.setImageResource(R.drawable.profile_user_icon)
        } else {
            Glide.with(this@EditProfileFragment)
                .load(user.userPic)
                .placeholder(R.drawable.profile_user_icon)
                .into(binding.profileImage)
        }
    }

    private fun pickImageFromGallery() {
        contract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                this,
                "Permission is required to upload profile photo",
                PermissionCodes.READ_EXTERNAL_STORAGE_PERMISSION_CODE,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Permission is required to upload profile photo",
                PermissionCodes.READ_EXTERNAL_STORAGE_PERMISSION_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(requireContext()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}