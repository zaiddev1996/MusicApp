package com.appsfactory.test.domain.model.artist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    val name: String,
    val url: String,
    val imageUrl: String
) : Parcelable
