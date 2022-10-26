package com.appsfactory.test.utils.extensions

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.appsfactory.test.utils.emptyString

fun Fragment.showToast(msg: String? = emptyString(), length: Int = Toast.LENGTH_SHORT) {
    requireContext().showToast(msg = msg, length = length)
}

fun Fragment.progressDialog() = requireContext().progressDialog()

fun Fragment.openUrl(url: String) {
    if (!url.startsWith("https://") && !url.startsWith("http://")) {
        return
    }
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}