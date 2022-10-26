package com.appsfactory.test.domain.model.track

import android.os.Parcelable
import com.appsfactory.test.domain.model.artist.Artist
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val name: String,
    val url: String,
    val artist: Artist,
    val duration: String
) : Parcelable
