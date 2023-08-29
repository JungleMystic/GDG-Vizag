package com.lrm.gdgvizag.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.model.Event
import com.lrm.gdgvizag.model.EventRegistration
import de.hdodenhof.circleimageview.CircleImageView

class ShowQrCodeDialog(private val mActivity: Activity, val context: Context) {

    private lateinit var infoDialog: AlertDialog

    fun showInfo(qrCode: Bitmap, eventReg: EventRegistration, event: Event) {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_qr_code_dialog, null)
        val eventImage = dialogView.findViewById<CircleImageView>(R.id.event_image_qr_d)
        val eventName = dialogView.findViewById<TextView>(R.id.event_name_qr_d)
        val eventDate = dialogView.findViewById<TextView>(R.id.event_date_qr_d)
        val eventType = dialogView.findViewById<TextView>(R.id.event_type_qr_d)

        val qrCodeImage = dialogView.findViewById<ImageView>(R.id.qr_code_d)
        val profileName = dialogView.findViewById<TextView>(R.id.profile_name_qr_d)
        val profileMail = dialogView.findViewById<TextView>(R.id.profile_mail_qr_d)

        Glide.with(context).load(event.imageUrl).placeholder(R.drawable.loading_icon_anim).into(eventImage)
        eventName.text = event.eventName
        eventDate.text = event.eventDate
        eventType.text = event.eventType

        qrCodeImage.setImageBitmap(qrCode)

        profileName.text = eventReg.applicantName
        profileMail.text = eventReg.mailId

        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(true)
        infoDialog = builder.create()
        infoDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        infoDialog.show()
    }
}