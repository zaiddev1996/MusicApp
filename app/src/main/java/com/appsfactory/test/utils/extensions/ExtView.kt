package com.appsfactory.test.utils.extensions

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible

inline fun SearchView.doOnQuerySubmit(crossinline action: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let { action(it) }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    })
}

fun View.show() {
    isVisible = true
}

fun View.hide() {
    isVisible = false
}