package com.appsfactory.test.domain.model.album

import android.os.Parcelable
import com.appsfactory.test.domain.model.artist.Artist
import com.appsfactory.test.domain.model.track.Track
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val name: String,
    val url: String,
    val artist: Artist,
    val imageUrl: String,
    val tracks: List<Track>?
) : Parcelable
