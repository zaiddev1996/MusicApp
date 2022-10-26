package com.appsfactory.test.data.local.repository

import com.appsfactory.test.data.local.room.AlbumDao
import com.appsfactory.test.data.mappers.toAlbum
import com.appsfactory.test.data.mappers.toAlbumDto
import com.appsfactory.test.data.mappers.toAlbumsDtoList
import com.appsfactory.test.data.mappers.toAlbumsList
import com.appsfactory.test.domain.model.album.Album
import com.appsfactory.test.domain.repository.local.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl @Inject constructor(
    private val albumsDao: AlbumDao
) : LocalRepository {

    override suspend fun insertAlbum(album: Album) {
        albumsDao.insertAlbum(album.toAlbumDto())
    }

    override suspend fun insertAlbums(albums: List<Album>) {
        albumsDao.insertAlbums(albums.toAlbumsDtoList())
    }

    override suspend fun deleteAlbum(album: Album) {
        albumsDao.deleteAlbum(album.toAlbumDto())
    }

    override suspend fun deleteAllAlbums() {
        albumsDao.deleteAllAlbums()
    }

    override suspend fun isExists(name: String): Boolean {
        return albumsDao.isExists(name = name)
    }

    override suspend fun getAlbum(name: String): Album? {
        return albumsDao.getAlbum(name = name)?.toAlbum()
    }

    override fun getAllAlbums(): Flow<List<Album>> {
        return albumsDao.getAllAlbums().map { it.toAlbumsList() }
    }
}