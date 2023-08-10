package com.lrm.gdgvizag.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.constants.SERVER_CONNECTION
import com.lrm.gdgvizag.constants.SERVER_STATUS
import com.lrm.gdgvizag.constants.STATUS
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private var status: String = "false"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appNameAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.app_name_anim)
        binding.appName.startAnimation(appNameAnim)

        val db = Firebase.firestore
        db.collection(SERVER_CONNECTION).document(SERVER_STATUS).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.i(TAG, "Splash Screen: server_connection_status -> ${document.data}")
                    status = document.data?.get(STATUS) as String
                    Log.i(TAG, "Splash Screen: server_connection_status status -> $status")
                }
            }
            .addOnFailureListener {
                status = "false"
            }

        Handler(Looper.getMainLooper()).postDelayed({
            if (status != "") {
                if (status == "true") {
                    val action =
                        SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
                    this.findNavController().navigate(action)
                } else {
                    val action =
                        SplashScreenFragmentDirections.actionSplashScreenFragmentToServerConnectionFragment()
                    this.findNavController().navigate(action)
                }
            }
        }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}