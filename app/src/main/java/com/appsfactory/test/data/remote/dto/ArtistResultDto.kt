package com.appsfactory.test.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArtistResultDto(
    @SerializedName("results")
    val results: ArtistResultDto
) {

    data class ArtistResultDto(
        @SerializedName("artistmatches")
        val artistMatches: MatchedArtistDto
    )

    data class MatchedArtistDto(
        @SerializedName("artist")
        val artists: List<ArtistDto>
    )

    data class ArtistDto(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("image")
        val images: List<ArtistImageDto>?
    )

    data class ArtistImageDto(
        @SerializedName("#text")
        val url: String
    )
}
