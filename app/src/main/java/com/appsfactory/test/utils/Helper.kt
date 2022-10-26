package com.appsfactory.test.utils

import android.util.Log

inline fun <reified T> logDebug(msg: String?) {
    Log.d(T::class.java.simpleName, msg.toString())
}

inline fun <reified T> logError(msg: String?) {
    Log.e(T::class.java.simpleName, msg.toString())
}

inline fun <reified T> logInfo(msg: String?) {
    Log.i(T::class.java.simpleName, msg.toString())
}

fun emptyString() = String()

fun randomColor() = (Math.random() * 16777215).toInt() or (0xFF shl 24)