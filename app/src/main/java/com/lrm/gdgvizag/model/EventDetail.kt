package com.lrm.gdgvizag.model

data class EventDetail(
    val eventId: String = "",
    val eventName: String = "",
    val eventGallery: List<String> = listOf(),
    val eventLogo: String = "",
    val eventWhen: String = "",
    val eventWhere: String = "",
    val eventLocation: String = "",
    val speakersList: List<Speaker> = listOf(),
    val agendaMap: Map<String, String> = mapOf()
)
