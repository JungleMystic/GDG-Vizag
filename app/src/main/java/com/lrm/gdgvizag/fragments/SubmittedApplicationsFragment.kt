package com.lrm.gdgvizag.fragments

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.lrm.gdgvizag.adapter.SubmittedApplicationsAdapter
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentSubmittedApplicationsBinding
import com.lrm.gdgvizag.model.Event
import com.lrm.gdgvizag.model.EventRegistration
import com.lrm.gdgvizag.model.QrCode
import com.lrm.gdgvizag.utils.LoadingDialog
import com.lrm.gdgvizag.utils.ShowQrCodeDialog
import com.lrm.gdgvizag.viewmodel.AppViewModel
import com.lrm.gdgvizag.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Hashtable

class SubmittedApplicationsFragment : Fragment() {

    private var _binding: FragmentSubmittedApplicationsBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var storageRef: StorageReference
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var userMail: String

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

        userMail = profileViewModel.userProfile.value!!.userMail
        appViewModel.getSubmittedApplications(userMail)

        binding.swipeRefreshLayout.setOnRefreshListener {
            appViewModel.getSubmittedApplications(userMail)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        appViewModel.submittedApplicationList.observe(viewLifecycleOwner) {list ->
            if (list.isNotEmpty()) {
                Log.i(TAG, "onViewCreated: Submitted Applications -> $list")
                binding.noApplicationsFound.visibility = View.GONE
                binding.submittedRv.visibility = View.VISIBLE

                val adapter = SubmittedApplicationsAdapter(
                    requireContext(), list,
                    appViewModel.eventsList.value!!
                ) {
                    eventRegistration, event ->
                    generateTicket(eventRegistration, event)
                    Toast.makeText(requireContext(), "Generating Ticket...", Toast.LENGTH_SHORT).show()
                }
                binding.submittedRv.adapter = adapter
                adapter.setOnClickListener(object : SubmittedApplicationsAdapter.OnClickListener {
                    override fun onClick() {
                        val action = SubmittedApplicationsFragmentDirections.actionSubmittedApplicationsFragmentToYourTicketsFragment()
                        this@SubmittedApplicationsFragment.findNavController().navigate(action)
                    }
                })
            } else {
                binding.noApplicationsFound.visibility = View.VISIBLE
                binding.submittedRv.visibility = View.INVISIBLE
            }
        }
    }

    private fun generateTicket(eventReg: EventRegistration, event: Event) {
        loadingDialog = LoadingDialog(requireActivity())

        val qrCode = generateQrCode(eventReg.mailId)
        val customQrCodeDialog = ShowQrCodeDialog(requireActivity(), requireContext())
        customQrCodeDialog.showInfo(qrCode, eventReg, event)

        uploadToFireStore(qrCode, eventReg)
    }

    private fun uploadToFireStore(qrCode: Bitmap, eventReg: EventRegistration) {

        val eventId = eventReg.eventId
        val mailId = eventReg.mailId

        storageRef = FirebaseStorage.getInstance().reference
        loadingDialog.startLoading()

        val baos = ByteArrayOutputStream()
        qrCode.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val reference = storageRef.child("gdg_vizag/qr_codes/$eventId/$mailId")
        reference.putBytes(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    reference.downloadUrl.addOnSuccessListener { task ->
                        uploadDataToFirestore(task.toString(), eventReg)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "File not uploaded", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadDataToFirestore(qrCodeLink: String, eventReg: EventRegistration) {

        val applicationId = eventReg.applicationId

        val db = Firebase.firestore
        val qrCode = QrCode("false", qrCodeLink, eventReg.eventId, eventReg.mailId)

        lifecycleScope.launch {
            db.collection("QR_Codes").document(eventReg.applicationId).set(qrCode)
                .addOnSuccessListener {
                    loadingDialog.dismissDialog()
                    db.collection("eventRegistration").document(applicationId).update("ticketGenerated", "true")
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully updated!")
                            appViewModel.getSubmittedApplications(userMail)
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

                    Toast.makeText(requireContext(), "Ticket Generated Successfully...", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    loadingDialog.dismissDialog()
                    Toast.makeText(requireContext(), "An error occurred...", Toast.LENGTH_SHORT).show()
                }
        }
    }

    @Throws(WriterException::class)
    private fun generateQrCode(mailId: String): Bitmap {
        val hintMap = Hashtable<EncodeHintType, ErrorCorrectionLevel>()
        hintMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(mailId, BarcodeFormat.QR_CODE, 300, 300)
        val width = bitMatrix.width
        val bmp = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565)
        for(x in 0 until width) {
            for (y in 0 until width) {
                bmp.setPixel(y, x, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}