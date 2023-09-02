package com.lrm.gdgvizag.model

data class QrCode(
    val scanned: String = "",
    val qrCodeImageLink: String = "",
    val applicationId: String = "",
    val mailId: String = "",
    val eventId: String = "",
    val scannedTime: String = ""
)