package com.appsfactory.test.domain.repository.local

import com.appsfactory.test.domain.model.album.Album
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun insertAlbum(album: Album)

    suspend fun insertAlbums(albums: List<Album>)

    suspend fun deleteAlbum(album: Album)

    suspend fun deleteAllAlbums()

    suspend fun isExists(name: String): Boolean

    suspend fun getAlbum(name: String): Album?

    fun getAllAlbums(): Flow<List<Album>>
}