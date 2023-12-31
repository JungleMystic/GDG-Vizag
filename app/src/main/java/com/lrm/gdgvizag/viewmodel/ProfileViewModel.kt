package com.lrm.gdgvizag.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.constants.USERS
import com.lrm.gdgvizag.model.User
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class ProfileViewModel: ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = Firebase.firestore

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> get() = _userProfile

    fun getUserProfile() {
        Handler(Looper.myLooper()!!).postDelayed({
            Log.i(TAG, "getUserProfile is called")
            Log.i(TAG, "getUserProfile _userProfile -> ${userProfile.value}")

            val userEmail = auth.currentUser?.email!!
            val userRef = db.collection(USERS).document(userEmail)
            viewModelScope.launch {
                userRef.get().addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    setUserProfile(user!!)
                }
                    .addOnFailureListener { Log.i(TAG, "getUserProfile: User not found") }
            }
        }, 2000)
    }


    fun setUserProfile(user: User?) {
        Log.i(TAG, "setUserProfile: $user")
        _userProfile.value = user
    }

    fun checkDataToUpdate(name: String, mail: String,
        phoneNum: String, gender: String): Boolean {

        val mobileNumPattern = Pattern.compile("[6-9][0-9]{9}")

        return name.isNotEmpty() && mail.isNotEmpty() && phoneNum.isNotEmpty() &&
                mobileNumPattern.matcher(phoneNum).matches() && gender.isNotEmpty()
    }
}