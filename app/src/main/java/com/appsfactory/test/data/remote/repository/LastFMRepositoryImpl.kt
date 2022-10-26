package com.appsfactory.test.data.remote.repository

import com.appsfactory.test.data.mappers.toAlbums
import com.appsfactory.test.data.mappers.toArtists
import com.appsfactory.test.data.mappers.toTracks
import com.appsfactory.test.data.remote.LastFMApi
import com.appsfactory.test.domain.model.album.Album
import com.appsfactory.test.domain.model.artist.Artist
import com.appsfactory.test.domain.model.track.Track
import com.appsfactory.test.domain.repository.local.LocalRepository
import com.appsfactory.test.domain.repository.remote.LastFMRepository
import com.appsfactory.test.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastFMRepositoryImpl @Inject constructor(
    private val api: LastFMApi,
    private val localRepository: LocalRepository
) : LastFMRepository, BaseRepository() {

    override suspend fun searchArtist(name: String): Flow<Result<List<Artist>>> {
        return makeRequest(
            request = {
                api.searchArtist(name = name)
            },
            response = {
                toArtists()
            }
        )
    }

    override suspend fun getTopAlbumsOfArtist(name: String): Flow<Result<List<Album>>> {
        return makeRequest(
            request = {
                api.getTopAlbums(name = name)
            },
            response = {
                toAlbums()
            }
        )
    }

    override suspend fun getTracks(artist: String, album: String): Flow<Result<List<Track>>> {
        val tracks = localRepository.getAlbum(name = album)?.tracks
        return tracks?.let {
            flow {
                emit(Result.Success(it))
            }
        } ?: makeRequest(
            request = {
                api.getTracks(artist = artist, album = album)
            },
            response = {
                toTracks()
            }
        )
    }
}