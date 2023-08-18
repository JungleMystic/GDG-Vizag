package com.lrm.gdgvizag.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.lrm.gdgvizag.R

class LoadingDialog(private val mActivity: Activity) {
    private lateinit var loadDialog: AlertDialog

    fun startLoading() {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_loading_screen, null)
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        loadDialog = builder.create()
        loadDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadDialog.show()
    }

    fun dismissDialog() {
        loadDialog.dismiss()
    }
}