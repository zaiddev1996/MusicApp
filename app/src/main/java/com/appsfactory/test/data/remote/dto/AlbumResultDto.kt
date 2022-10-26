package com.appsfactory.test.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class AlbumResultDto(
    @SerializedName("topalbums")
    val topAlbums: TopAlbumDto
) {

    data class TopAlbumDto(
        @SerializedName("album")
        val albums: List<AlbumDto>
    )

    @Entity
    data class AlbumDto(
        @PrimaryKey
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("artist")
        val artistDto: ArtistResultDto.ArtistDto,
        @SerializedName("image")
        val images: List<AlbumImageDto>,
        val tracks: List<TrackResultDto.TrackDto>? = null
    )

    data class AlbumImageDto(
        @SerializedName("#text")
        val url: String
    )
}