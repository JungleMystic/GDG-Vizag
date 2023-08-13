package com.lrm.gdgvizag.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lrm.gdgvizag.constants.USERS
import com.lrm.gdgvizag.model.User
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private var db: FirebaseFirestore = Firebase.firestore

    private val _onlineStatus = MutableLiveData(false)
    val onlineStatus: LiveData<Boolean> get() = _onlineStatus

    private fun setOnlineStatus(status: Boolean) {
        _onlineStatus.value = status
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    fun checkForInternet(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                setOnlineStatus(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                setOnlineStatus(false)
            }
        })
    }

    fun checkUserInFirestore(context: Context, account: GoogleSignInAccount) {

        viewModelScope.launch {
            val userUid = FirebaseAuth.getInstance().currentUser?.uid!!

            val db = Firebase.firestore
            db.collection(USERS).document(account.email!!).get()
                .addOnSuccessListener { result ->
                    if (result.exists()) {
                        Toast.makeText(context, "Welcome back...", Toast.LENGTH_LONG).show()
                    } else {
                        val user = User(
                            account.displayName!!,
                            account.email!!,
                            "",
                            userUid,
                            "",
                            ""
                        )

                        db.collection(USERS).document(account.email!!).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Welcome to GDG Vizag", Toast.LENGTH_LONG)
                                    .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "An error occurred...", Toast.LENGTH_LONG)
                                    .show()
                            }
                    }
                }
        }
    }


}