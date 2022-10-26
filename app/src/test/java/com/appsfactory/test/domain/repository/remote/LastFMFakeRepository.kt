package com.appsfactory.test.domain.repository.remote

import com.appsfactory.test.domain.model.album.Album
import com.appsfactory.test.domain.model.artist.Artist
import com.appsfactory.test.domain.model.track.Track
import com.appsfactory.test.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LastFMFakeRepository : LastFMRepository {

    private var shouldReturnNetworkError = false

    private val artists = mutableListOf<Artist>()
    private val tracks = mutableListOf<Track>()
    private val albums = mutableListOf<Album>()

    override suspend fun searchArtist(name: String): Flow<Result<List<Artist>>> {
        return flow {
            if (shouldReturnNetworkError) {
                emit(Result.Error("Error occurred"))
            } else {
                emit(Result.Success(artists))
            }
        }
    }

    override suspend fun getTopAlbumsOfArtist(name: String): Flow<Result<List<Album>>> {
        return flow {
            if (shouldReturnNetworkError) {
                emit(Result.Error("Error occurred"))
            } else {
                emit(Result.Success(albums.filter { it.artist.name == name }))
            }
        }
    }

    override suspend fun getTracks(artist: String, album: String): Flow<Result<List<Track>>> {
        return flow {
            if (shouldReturnNetworkError) {
                emit(Result.Error("Error occurred"))
            } else {
                emit(Result.Success(tracks))
            }
        }
    }

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    fun updateArtists(artists: List<Artist>) {
        this.artists.addAll(artists)
    }

    fun updateTracks(tracks: List<Track>) {
        this.tracks.addAll(tracks)
    }

    fun updateAlbums(albums: List<Album>) {
        this.albums.addAll(albums)
    }
}