package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentScanQrCodeBinding
import com.lrm.gdgvizag.model.Event
import com.lrm.gdgvizag.model.EventRegistration
import com.lrm.gdgvizag.utils.LoadingDialog
import com.lrm.gdgvizag.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScanQrCodeFragment : Fragment() {

    private var _binding: FragmentScanQrCodeBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var applicationId: String

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

        appViewModel.scanResult.observe(viewLifecycleOwner) {scanValue ->
            if (scanValue != "" && scanValue != "-----") {
                binding.applicationId.text = scanValue
                getDataFromFirebase(scanValue)
            } else binding.applicationId.text = "-----"
        }

        binding.scanFab.setOnClickListener { setupScanner() }
    }

    private fun getDataFromFirebase(applicationId: String) {
        appViewModel.getApplication(requireContext(), applicationId)
        appViewModel.getScannedTicketInfo(applicationId)
        appViewModel.applicationResult.observe(viewLifecycleOwner) {application ->
            if (application != null) {
                binding.applicationCard.visibility = View.VISIBLE
                binding.noApplicationsFound.visibility = View.GONE
                val event = appViewModel.eventsList.value?.single { it.eventId == application.eventId }
                if (event != null) {
                    bindEventData(event)
                }
                if (application.ticketGenerated == "true") {
                    bindApplicantData(application)
                } else Toast.makeText(requireContext(), "Ticket not generated", Toast.LENGTH_SHORT).show()

            } else {
                binding.applicationCard.visibility = View.GONE
                binding.noApplicationsFound.visibility = View.VISIBLE
            }
        }

        appViewModel.scannedTicketResult.observe(viewLifecycleOwner) { ticket ->
            if (ticket != null) {
                if (ticket.scanned == "false") {
                    binding.checkInButton.visibility = View.VISIBLE
                    binding.ticketScannedAlready.visibility = View.GONE
                    binding.scannedTime.visibility = View.GONE
                    binding.checkInButton.setOnClickListener {
                        checkInAttendee()
                    }
                } else {
                    binding.checkInButton.visibility = View.GONE
                    binding.ticketScannedAlready.visibility = View.VISIBLE
                    binding.scannedTime.visibility = View.VISIBLE
                    binding.scannedTime.text = ticket.scannedTime
                }
            }
        }
    }

    private fun checkInAttendee() {
        val loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()

        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
        val currentDate = sdf.format(Date())

        val db = Firebase.firestore
        db.collection("QR_Codes").document(applicationId).update(
            "scanned", "true",
            "scannedTime", currentDate
            )
            .addOnSuccessListener {
                getDataFromFirebase(applicationId)
                loadingDialog.dismissDialog()
                Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

    }

    private fun bindEventData(event: Event) {
        Glide.with(requireContext()).load(event.imageUrl).placeholder(R.drawable.loading_icon_anim)
            .into(binding.eventImage)
        binding.eventName.text = event.eventName
        binding.eventType.text = event.eventType
        binding.eventDate.text = event.eventDate
    }

    private fun bindApplicantData(application: EventRegistration) {
        applicationId = application.applicationId
        binding.applicantName.text = application.applicantName
        binding.profileMail.text = application.mailId
    }

    private fun setupScanner() {
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

        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(requireContext(), options)
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                appViewModel.setScanResult(barcode.rawValue.toString())
            }
            .addOnCanceledListener {
                Toast.makeText(requireContext(), "Scanning is canceled...", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e: Exception ->
                Log.i(TAG, "setupScanner: Exception $e")
                Toast.makeText(requireContext(), "Scanning is failed...", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearData() {
        appViewModel.setScanResult("-----")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearData()
        _binding = null
    }
}