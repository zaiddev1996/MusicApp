package com.appsfactory.test.domain.repository.remote

import com.appsfactory.test.domain.model.album.Album
import com.appsfactory.test.domain.model.artist.Artist
import com.appsfactory.test.domain.model.track.Track
import com.appsfactory.test.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface LastFMRepository {
    suspend fun searchArtist(name: String): Flow<Result<List<Artist>>>

    suspend fun getTopAlbumsOfArtist(name: String): Flow<Result<List<Album>>>

    suspend fun getTracks(artist: String, album: String): Flow<Result<List<Track>>>
}