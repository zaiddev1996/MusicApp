package com.appsfactory.test.data.remote

import com.appsfactory.test.data.remote.dto.AlbumResultDto
import com.appsfactory.test.data.remote.dto.ArtistResultDto
import com.appsfactory.test.data.remote.dto.TrackResultDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFMApi {

    @GET("?method=artist.search")
    suspend fun searchArtist(@Query("artist") name: String): Response<ArtistResultDto>

    @GET("?method=artist.gettopalbums")
    suspend fun getTopAlbums(@Query("artist") name: String): Response<AlbumResultDto>

    @GET("?method=album.getinfo")
    suspend fun getTracks(
        @Query("artist") artist: String,
        @Query("album") album: String
    ): Response<TrackResultDto>
}