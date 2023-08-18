package com.lrm.gdgvizag.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lrm.gdgvizag.R
import com.lrm.gdgvizag.model.Organizer
import de.hdodenhof.circleimageview.CircleImageView

class OrganizerInfoDialog(private val mActivity: Activity, val context: Context) {

    private lateinit var infoDialog: AlertDialog

    fun showInfo(organizer: Organizer) {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_organizer_info_screen, null)
        val organizerImage = dialogView.findViewById<ImageView>(R.id.org_image_d)
        val organizerName = dialogView.findViewById<TextView>(R.id.org_name_d)
        val organizerOrg = dialogView.findViewById<TextView>(R.id.org_d)
        val organizerTitle = dialogView.findViewById<TextView>(R.id.org_title_d)
        val organizerAboutTitle = dialogView.findViewById<TextView>(R.id.about_me_title)
        val organizerAbout = dialogView.findViewById<TextView>(R.id.about_me_info)

        val twitterX = dialogView.findViewById<CircleImageView>(R.id.twitter_x_d)
        val linkedIn = dialogView.findViewById<CircleImageView>(R.id.linked_in_d)
        val fb = dialogView.findViewById<CircleImageView>(R.id.facebook_d)

        Glide.with(context).load(organizer.imageUrl).placeholder(R.drawable.loading_icon_anim).into(organizerImage)
        organizerName.text = organizer.name
        organizerOrg.text = organizer.orgName
        organizerTitle.text = organizer.title
        organizerAbout.text = organizer.aboutMe.replace("/n", "\n")

        if (organizer.twitterXLink != "") {
            twitterX.visibility = View.VISIBLE
        }

        if (organizer.linkedIn != "") {
            linkedIn.visibility = View.VISIBLE
        }

        if (organizer.fbLink != "") {
            fb.visibility = View.VISIBLE
        }

        twitterX.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(organizer.twitterXLink))
            context.startActivity(intent)
        }

        linkedIn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(organizer.linkedIn))
            context.startActivity(intent)
        }

        fb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(organizer.fbLink))
            context.startActivity(intent)
        }

        if (organizer.aboutMe == "") {
            organizerAboutTitle.visibility = View.INVISIBLE
        }

        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(true)
        infoDialog = builder.create()
        infoDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        infoDialog.show()
    }
}