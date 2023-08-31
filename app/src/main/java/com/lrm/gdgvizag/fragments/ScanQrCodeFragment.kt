package com.lrm.gdgvizag.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.fragment.app.Fragment
import com.google.common.util.concurrent.ListenableFuture
import com.lrm.gdgvizag.constants.PermissionCodes
import com.lrm.gdgvizag.databinding.FragmentScanQrCodeBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.util.concurrent.ExecutorService

class ScanQrCodeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentScanQrCodeBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanQrCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasPermission()) requestPermission()

    }

    private fun scanQrCode() {

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
        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Permission is required to make call",
            PermissionCodes.CAMERA_PERMISSION_CODE,
            Manifest.permission.CALL_PHONE
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