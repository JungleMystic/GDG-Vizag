package com.lrm.gdgvizag.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val eventId: String = "",
    val eventName: String = "",
    val eventType: String = "",
    val eventStatus: String = "",
    val eventDate: String = "",
    val imageUrl: String = "",
    val registrationStatus: String = "",
    val registrationText: String = ""
): Parcelable
