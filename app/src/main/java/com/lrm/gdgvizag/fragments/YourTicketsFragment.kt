package com.lrm.gdgvizag.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.lrm.gdgvizag.adapter.YourTicketsAdapter
import com.lrm.gdgvizag.constants.PermissionCodes
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentYourTicketsBinding
import com.lrm.gdgvizag.viewmodel.AppViewModel
import com.lrm.gdgvizag.viewmodel.ProfileViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class YourTicketsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

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
            if (hasPermission()) {
                val action = YourTicketsFragmentDirections.actionYourTicketsFragmentToScanQrCodeFragment()
                this.findNavController().navigate(action)
            } else {
                requestPermission()
            }
        }
    }

    private fun hasPermission(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.CAMERA)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(requireContext()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        val moduleInstall = ModuleInstall.getClient(requireContext())
        val installRequest = ModuleInstallRequest.newBuilder()
            .addApi(GmsBarcodeScanning.getClient(requireContext()))
            .build()

        moduleInstall.installModules(installRequest)
            .addOnSuccessListener {
                if (it.areModulesAlreadyInstalled()){
                    Log.i(TAG, "setupScanner: ModuleInstallRequest -> Modules are already installed when the request is sent.")
                }
            }
            .addOnFailureListener {
                Log.i(TAG, "setupScanner: ModuleInstallRequest is failed.")
            }

        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Permission is required to make call",
            PermissionCodes.CAMERA_PERMISSION_CODE,
            Manifest.permission.CAMERA
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //EasyPermissions handles the request result
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}