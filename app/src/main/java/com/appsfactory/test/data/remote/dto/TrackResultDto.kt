package com.appsfactory.test.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TrackResultDto(
    @SerializedName("album")
    val album: AlbumDetailDto
) {

    data class AlbumDetailDto(
        @SerializedName("tracks")
        val tracksInfo: TrackInfoDto?
    )

    data class TrackInfoDto(
        @SerializedName("track")
        val tracks: List<TrackDto>?
    )

    data class TrackDto(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("duration")
        val duration: Long?,
        @SerializedName("artist")
        val artistDto: ArtistResultDto.ArtistDto
    )
}