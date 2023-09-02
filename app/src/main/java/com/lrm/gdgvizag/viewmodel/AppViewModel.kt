package com.lrm.gdgvizag.viewmodel

import android.app.Activity
import android.content.Context
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
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.constants.USERS
import com.lrm.gdgvizag.model.Event
import com.lrm.gdgvizag.model.EventDetail
import com.lrm.gdgvizag.model.EventRegistration
import com.lrm.gdgvizag.model.Organizer
import com.lrm.gdgvizag.model.Partner
import com.lrm.gdgvizag.model.QrCode
import com.lrm.gdgvizag.model.User
import com.lrm.gdgvizag.utils.LoadingDialog
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private var db: FirebaseFirestore = Firebase.firestore

    private val _imagesList = MutableLiveData<MutableList<String>>(mutableListOf())
    val imagesList: LiveData<MutableList<String>> get() = _imagesList

    private val _eventsList = MutableLiveData<MutableList<Event>>(mutableListOf())
    val eventsList: LiveData<MutableList<Event>> get() = _eventsList

    private val _upcomingEventsList = MutableLiveData<MutableList<Event>>(mutableListOf())
    val upcomingEventsList: LiveData<MutableList<Event>> get() = _upcomingEventsList

    private val _pastEventsList = MutableLiveData<MutableList<Event>>(mutableListOf())
    val pastEventsList: LiveData<MutableList<Event>> get() = _pastEventsList

    private val _partnersList = MutableLiveData<MutableList<Partner>>(mutableListOf())
    val partnersList: LiveData<MutableList<Partner>> get() = _partnersList

    private val _organizersList = MutableLiveData<MutableList<Organizer>>(mutableListOf())
    val organizersList: LiveData<MutableList<Organizer>> get() = _organizersList

    private val _eventDetail = MutableLiveData<EventDetail>()
    val eventDetail: LiveData<EventDetail> get() = _eventDetail

    private val _submittedApplicationList =
        MutableLiveData<MutableList<EventRegistration>>(mutableListOf())
    val submittedApplicationList: LiveData<MutableList<EventRegistration>> get() = _submittedApplicationList

    private val _yourTicketsList = MutableLiveData<MutableList<QrCode>>(mutableListOf())
    val yourTicketsList: LiveData<MutableList<QrCode>> get() = _yourTicketsList

    private val _scanResult = MutableLiveData<String>("")
    val scanResult: LiveData<String> get() = _scanResult

    private val _applicationResult = MutableLiveData<EventRegistration?>()
    val applicationResult: LiveData<EventRegistration?> get() = _applicationResult

    /*private val _onlineStatus = MutableLiveData(false)
    val onlineStatus: LiveData<Boolean> get() = _onlineStatus*/

    fun setScanResult(result: String) {
        _scanResult.value = result
        Log.i(TAG, "setBarcodeList: ${_scanResult.value}")
    }


    fun getImages() {
        Log.i(TAG, "getImages is called")
        viewModelScope.launch {
            db.collection("gdg_memories").document("images").get()
                .addOnSuccessListener { document ->
                    //Log.i(TAG, "getImages: Images data -> ${document.get("imageUrls")}")
                    _imagesList.value = document.get("imageUrls") as MutableList<String>?
                }
                .addOnFailureListener { exception ->
                    Log.i(TAG, "getImages: Error getting documents: ", exception)
                }
        }
    }

    fun getEventsData() {
        Log.i(TAG, "getEventsData is called")
        viewModelScope.launch {
            db.collection("events").get()
                .addOnSuccessListener { documents ->

                    _eventsList.value?.clear()
                    _upcomingEventsList.value?.clear()
                    _pastEventsList.value?.clear()

                    for (document in documents) {
                        //Log.i(TAG, "getEvents: Events Data -> ${document.data}")
                        val event = document.toObject(Event::class.java)
                        _eventsList.value?.add(event)
                        //Log.i(TAG, "getEvents: Events Data event-> $event ")
                        if (event.eventStatus == "upcoming") {
                            _upcomingEventsList.value?.add(event)
                        } else if (event.eventStatus == "past") {
                            _pastEventsList.value?.add(event)
                        }
                    }
                    _eventsList.postValue(_eventsList.value)
                    //Log.i(TAG, "getEvents: Upcoming Events Data -> ${_upcomingEventsList.value} ")
                    _upcomingEventsList.postValue(_upcomingEventsList.value)
                    //Log.i(TAG, "getEvents: Past Events Data -> ${_pastEventsList.value} ")
                    _pastEventsList.postValue(_pastEventsList.value)
                }
        }
    }

    fun getPartnersData() {
        Log.i(TAG, "getPartnersData is called")
        viewModelScope.launch {
            db.collection("partners").get()
                .addOnSuccessListener { documents ->

                    _partnersList.value?.clear()

                    for (document in documents) {
                        //Log.i(TAG, "getPartners: Partners Data -> ${document.data}")
                        val partner = document.toObject(Partner::class.java)
                        //Log.i(TAG, "getPartner: Single Partner-> $partner")
                        _partnersList.value?.add(partner)
                    }
                    //Log.i(TAG, "getPartner: Partner List Data -> ${_partnersList.value} ")
                    _partnersList.postValue(_partnersList.value)
                }
        }
    }

    fun getOrganizersData() {
        Log.i(TAG, "getOrganizersData is called")
        viewModelScope.launch {
            db.collection("organizers").get()
                .addOnSuccessListener { documents ->

                    _organizersList.value?.clear()

                    for (document in documents) {
                        //Log.i(TAG, "getOrganizers: Organizers Data -> ${document.data}")
                        val organizer = document.toObject(Organizer::class.java)
                        //Log.i(TAG, "getOrganizer: Single Organizer-> $organizer")
                        _organizersList.value?.add(organizer)
                    }
                    //Log.i(TAG, "getOrganizers: Organizers List Data -> ${_organizersList.value} ")
                    _organizersList.postValue(_organizersList.value)
                }
        }
    }

    fun getEventDetailedData(eventId: String, activity: Activity) {
        val loadingDialog = LoadingDialog(activity)
        loadingDialog.startLoading()
        viewModelScope.launch {
            db.collection("event_detail").document(eventId).get()
                .addOnSuccessListener { result ->
                    if (result.exists()) {
                        loadingDialog.dismissDialog()
                        //Log.i(TAG, "getEventDetailedData: result data -> ${result.data}")
                        val eventDetail = result.toObject(EventDetail::class.java)
                        //Log.i(TAG, "getEventDetailedData: $eventDetail")
                        _eventDetail.value = eventDetail!!
                        //Log.i(TAG, "getEventDetailedData: _eventDetail.value -> ${_eventDetail.value}")
                    }
                }
        }
    }

    fun getApplication(context: Context, applicationId: String) {
        viewModelScope.launch {
            db.collection("eventRegistration").document(applicationId).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val application = it.toObject(EventRegistration::class.java)
                        _applicationResult.value = application!!
                    } else {
                        _applicationResult.value = null
                        Toast.makeText(context, "Application not found...", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Log.i(TAG, "getApplication: exception -> ${it.message}")
                }
        }
    }

    fun getSubmittedApplications(mailId: String) {
        viewModelScope.launch {
            db.collection("eventRegistration").get()
                .addOnSuccessListener { documents ->
                    _submittedApplicationList.value?.clear()

                    for (document in documents) {
                        val application = document.toObject(EventRegistration::class.java)
                        if (application.mailId == mailId) {
                            _submittedApplicationList.value?.add(application)
                        }
                    }
                    _submittedApplicationList.postValue(_submittedApplicationList.value)
                    Log.i(TAG, "getSubmittedApplications: ${_submittedApplicationList.value}")
                }
        }
    }

    fun getTickets(mailId: String) {
        viewModelScope.launch {
            db.collection("QR_Codes").get()
                .addOnSuccessListener { documents ->
                    _yourTicketsList.value?.clear()

                    for (document in documents) {
                        val ticket = document.toObject(QrCode::class.java)
                        if (ticket.mailId == mailId) {
                            _yourTicketsList.value?.add(ticket)
                        }
                    }
                    _yourTicketsList.postValue(_yourTicketsList.value)
                    Log.i(TAG, "getTickets: ${_yourTicketsList.value}")
                }
        }
    }


    fun checkUserInFirestore(context: Context, account: GoogleSignInAccount) {

        viewModelScope.launch {
            val userUid = FirebaseAuth.getInstance().currentUser?.uid!!

            db.collection(USERS).document(account.email!!).get()
                .addOnSuccessListener { result ->
                    if (result.exists()) {
                        Log.i(TAG, "checkUserInFirestore: result exists -> ${result.data}")
                        Toast.makeText(context, "Welcome back...", Toast.LENGTH_LONG).show()
                    } else {
                        val user = User(
                            account.displayName!!,
                            account.email!!,
                            "",
                            userUid,
                            "",
                            "",
                            "false"
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

    /*
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
        }*/
}