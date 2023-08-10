package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lrm.gdgvizag.constants.SERVER_CONNECTION
import com.lrm.gdgvizag.constants.SERVER_STATUS
import com.lrm.gdgvizag.constants.STATUS
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentServerConnectionBinding

class ServerConnectionFragment : Fragment() {

    private var _binding: FragmentServerConnectionBinding? = null
    private val binding get() = _binding!!

    private var status: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServerConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore
        val docRef = db.collection(SERVER_CONNECTION).document(SERVER_STATUS)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.i(TAG, "ServerConnectionFragment: error message $e")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                Log.i(TAG, "ServerConnectionFragment: snapshot $snapshot")
                status = snapshot.data?.get(STATUS) as String
                Log.i(TAG, "ServerConnectionFragment: status value -> $status")
                goToLoginScreen(status)
            } else {
                Log.i(TAG, "ServerConnectionFragment: snapshot else block null")
            }
        }
    }

    private fun goToLoginScreen(status: String) {
        if (status == "true") {
            val action = ServerConnectionFragmentDirections.actionServerConnectionFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}