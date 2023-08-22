package com.lrm.gdgvizag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.constants.USERS
import com.lrm.gdgvizag.model.User

class ProfileViewModel: ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = Firebase.firestore

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> get() = _userProfile


    fun getUserProfile() {
        val userEmail = auth.currentUser?.email!!
        val userRef = db.collection(USERS).document(userEmail)
        userRef.get().addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject(User::class.java)
            setUserProfile(user!!)
        }
            .addOnFailureListener { Log.i(TAG, "getUserProfile: User not found") }
    }


    fun setUserProfile(user: User?) {
        Log.i(TAG, "setUserProfile: $user")
        _userProfile.value = user
    }

}