package com.lrm.gdgvizag.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.model.Partner

class InfoDialog(val mActivity: Activity, val context: Context) {

    private lateinit var infoDialog: AlertDialog

    fun showInfo(partner: Partner) {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_info_screen, null)
        val partnerImage = dialogView.findViewById<ImageView>(R.id.partner_image_d)
        val partnerName = dialogView.findViewById<TextView>(R.id.partner_name_d)
        val partnerInfo = dialogView.findViewById<TextView>(R.id.partner_info_d)


        Glide.with(context).load(partner.logo).placeholder(R.drawable.loading_icon_anim).into(partnerImage)
        partnerName.text = partner.name
        partnerInfo.text = partner.info

        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(true)
        infoDialog = builder.create()
        infoDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        infoDialog.show()
    }
}