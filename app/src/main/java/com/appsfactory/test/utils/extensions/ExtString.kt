package com.appsfactory.test.utils.extensions

fun String?.ifNull(block: () -> String) = this ?: block()