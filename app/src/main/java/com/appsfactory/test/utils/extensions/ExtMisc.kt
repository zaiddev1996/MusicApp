package com.appsfactory.test.utils.extensions

import androidx.appcompat.app.AlertDialog

fun AlertDialog.isVisible(flag: Boolean) = if (flag) show() else dismiss()