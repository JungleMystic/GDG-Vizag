package com.lrm.gdgvizag.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.databinding.FragmentSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

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

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        Handler(Looper.myLooper()!!).postDelayed({
            if (currentUser != null) {
                val action =
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
                findNavController().navigate(action)
            } else {
                val action =
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
                findNavController().navigate(action)
            }

        }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}