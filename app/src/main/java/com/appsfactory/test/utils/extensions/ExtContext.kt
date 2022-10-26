package com.appsfactory.test.utils.extensions

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.appsfactory.test.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showToast(msg: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg.toString(), length).show()
}

fun Context.progressDialog(): AlertDialog {
    return MaterialAlertDialogBuilder(this).apply {
        setView(R.layout.item_progress_dialog)
    }.create().apply {
        setCancelable(false)
        window?.apply {
            setDimAmount(0F)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}