package com.lrm.gdgvizag.model

data class EventRegistration(
    val applicationId: String = "",
    val eventId: String = "",
    val applicantName: String = "",
    val mailId: String = "",
    val phoneNum: String = "",
    val gender: String = "",
    val careerStatus: String = "",
    val orgUnivName: String = "",
    val techInterest: List<String> = listOf(),
    val whyThisEvent: String = "",
    val githubLink: String = "",
    val applicationStatus: String = "",
    val acceptanceStatus: String = "",
    val isTicketGenerated: String = ""
)
