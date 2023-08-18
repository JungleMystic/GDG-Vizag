package com.lrm.gdgvizag.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.model.AboutGDG
import de.hdodenhof.circleimageview.CircleImageView

class AboutGDGDialog(private val mActivity: Activity, val context: Context) {

    private lateinit var infoDialog: AlertDialog

    fun showInfo(aboutGDG: AboutGDG) {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_gdg_info_screen, null)
        val gdgImage = dialogView.findViewById<ImageView>(R.id.gdg_logo)
        val gdgName = dialogView.findViewById<TextView>(R.id.gdg_name)
        val aboutGDGInfo = dialogView.findViewById<TextView>(R.id.about_gdg)

        val webAddress = dialogView.findViewById<TextView>(R.id.gdg_web_address)
        val twitterX = dialogView.findViewById<CircleImageView>(R.id.gdg_twitter_x)
        val linkedIn = dialogView.findViewById<CircleImageView>(R.id.gdg_linked_in)
        val fb = dialogView.findViewById<CircleImageView>(R.id.gdg_fb)
        val insta = dialogView.findViewById<CircleImageView>(R.id.gdg_insta)
        val youtube = dialogView.findViewById<CircleImageView>(R.id.gdg_youtube)

        gdgImage.setImageResource(R.drawable.app_logo)
        gdgName.text = aboutGDG.name
        aboutGDGInfo.text = aboutGDG.about
        webAddress.text = aboutGDG.websiteLink

        webAddress.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutGDG.websiteLink))
            context.startActivity(intent)
        }

        twitterX.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutGDG.twitterXLink))
            context.startActivity(intent)
        }

        linkedIn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutGDG.linkedIn))
            context.startActivity(intent)
        }

        fb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutGDG.fbLink))
            context.startActivity(intent)
        }

        insta.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutGDG.instagramLink))
            context.startActivity(intent)
        }

        youtube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutGDG.youTubeLink))
            context.startActivity(intent)
        }

        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(true)
        infoDialog = builder.create()
        infoDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        infoDialog.show()
    }
}