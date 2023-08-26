package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentEventRegistrationBinding
import com.lrm.gdgvizag.model.Event
import com.lrm.gdgvizag.model.EventRegistration
import com.lrm.gdgvizag.model.User
import com.lrm.gdgvizag.utils.LoadingDialog
import com.lrm.gdgvizag.viewmodel.ProfileViewModel
import java.util.UUID
import java.util.regex.Pattern

class EventRegistrationFragment : Fragment() {

    private var _binding: FragmentEventRegistrationBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val eventRegArgs: EventRegistrationFragmentArgs by navArgs()
    private lateinit var event: Event
    private lateinit var techInterests: MutableList<String>
    private lateinit var loadingDialog: LoadingDialog

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

        binding.backIcon.setOnClickListener{ this.findNavController().navigateUp() }

        loadingDialog = LoadingDialog(requireActivity())

        event = eventRegArgs.event
        bindEventDetails(event)

        techInterests = mutableListOf()

        profileViewModel.userProfile.observe(viewLifecycleOwner) {user ->
            if (user != null) {
                bind(user)
            }
        }

        binding.techDomain1.setOnClickListener {
            if (binding.techDomain1.isChecked) {
                techInterests.add(binding.techDomain1.text.toString())
            } else {
                techInterests.remove(binding.techDomain1.text.toString())
            }
        }

        binding.techDomain2.setOnClickListener {
            if (binding.techDomain2.isChecked) {
                techInterests.add(binding.techDomain2.text.toString())
            } else {
                techInterests.remove(binding.techDomain2.text.toString())
            }
        }

        binding.techDomain3.setOnClickListener {
            if (binding.techDomain3.isChecked) {
                techInterests.add(binding.techDomain3.text.toString())
            } else {
                techInterests.remove(binding.techDomain3.text.toString())
            }
        }

        binding.techDomain4.setOnClickListener {
            if (binding.techDomain4.isChecked) {
                techInterests.add(binding.techDomain4.text.toString())
            } else {
                techInterests.remove(binding.techDomain4.text.toString())
            }
        }

        binding.techDomain5.setOnClickListener {
            if (binding.techDomain5.isChecked) {
                techInterests.add(binding.techDomain5.text.toString())
            } else {
                techInterests.remove(binding.techDomain5.text.toString())
            }
        }

        binding.techDomain6.setOnClickListener {
            if (binding.techDomain6.isChecked) {
                techInterests.add(binding.techDomain6.text.toString())
            } else {
                techInterests.remove(binding.techDomain6.text.toString())
            }
        }

        binding.techDomain7.setOnClickListener {
            if (binding.techDomain7.isChecked) {
                techInterests.add(binding.techDomain7.text.toString())
            } else {
                techInterests.remove(binding.techDomain7.text.toString())
            }
        }

        binding.submitButton.setOnClickListener {
            submitDataToFirestore()
        }
    }

    private fun submitDataToFirestore() {
        val eventId = event.eventId
        val name = binding.profileName.text.toString()
        val mail = binding.profileMail.text.toString()
        val phoneNum = binding.mobileNumber.text.toString()
        val gender = binding.genderActv.text.toString()
        val careerStatus = binding.csActv.text.toString()
        val orgUnivName = binding.profileOrg.text.toString()
        val whyThisEvent = binding.whyThisEvent.text.toString()

        if (isEntryValid(name, phoneNum, gender,
            careerStatus, orgUnivName, techInterests, whyThisEvent)) {
            loadingDialog.startLoading()

            val applicationId = "${eventId}_${UUID.randomUUID()}"
            val application = EventRegistration(applicationId, eventId, name, mail,
                phoneNum, gender, careerStatus, orgUnivName,
                techInterests, whyThisEvent, "",
                "Submitted", "Pending", "false")

            Log.i(TAG, "submitDataToFirestore: $application")
            uploadDataToFirestore(application)

        } else {
            Toast.makeText(requireContext(), "Please fill all the fields...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadDataToFirestore(application: EventRegistration) {
        val db = Firebase.firestore
        db.collection("eventRegistration").document(application.applicationId).set(application)
            .addOnSuccessListener {
                loadingDialog.dismissDialog()
                Toast.makeText(requireContext(), "Application submitted successfully...", Toast.LENGTH_SHORT).show()
                this.findNavController().navigateUp()
            }
            .addOnFailureListener {
                loadingDialog.dismissDialog()
                Toast.makeText(requireContext(), "An error occurred, please try again...", Toast.LENGTH_SHORT).show()
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
        binding.genderActv.setText(user.userGender, TextView.BufferType.SPANNABLE)
        binding.whyThisEventTil.hint = getString(R.string.why_this_event_question, event.eventName)
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

    private fun isEntryValid(
        name: String, phoneNum: String, gender: String,
        cs: String, orgUniv: String, techInt: List<String>,
        whyThisEvent: String
    ): Boolean {
        val mobileNumPattern = Pattern.compile("[6-9][0-9]{9}")
        return name.isNotEmpty() && phoneNum.isNotEmpty() && mobileNumPattern.matcher(phoneNum).matches()
                && gender.isNotEmpty() && cs.isNotEmpty() && orgUniv.isNotEmpty() && techInt.isNotEmpty()
                && whyThisEvent.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}