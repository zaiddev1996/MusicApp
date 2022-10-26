package com.appsfactory.test.domain.repository.local

import com.appsfactory.test.data.mappers.toAlbum
import com.appsfactory.test.data.mappers.toAlbumDto
import com.appsfactory.test.data.mappers.toAlbumsDtoList
import com.appsfactory.test.data.mappers.toAlbumsList
import com.appsfactory.test.data.remote.dto.AlbumResultDto
import com.appsfactory.test.domain.model.album.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class LocalFakeRepository : LocalRepository {

    private val albums = mutableListOf<AlbumResultDto.AlbumDto>()
    private val flow = MutableStateFlow<List<AlbumResultDto.AlbumDto>>(albums)

    fun addAlbums(value: List<Album>) {
        albums.addAll(value.toAlbumsDtoList())
        refreshFlow()
    }

    private fun refreshFlow() {
        flow.value = albums
    }

    override suspend fun insertAlbum(album: Album) {
        albums.add(album.toAlbumDto())
        refreshFlow()
    }

    override suspend fun deleteAlbum(album: Album) {
        albums.remove(album.toAlbumDto())
        refreshFlow()
    }

    override suspend fun isExists(name: String): Boolean {
        val album = albums.find { it.name == name }?.toAlbum()
        return album != null
    }

    override suspend fun getAlbum(name: String): Album? {
        return albums.find { it.name == name }?.toAlbum()
    }

    override fun getAllAlbums(): Flow<List<Album>> = flow.map { it.toAlbumsList() }

    override suspend fun deleteAllAlbums() {
        albums.clear()
        refreshFlow()
    }

    override suspend fun insertAlbums(albums: List<Album>) {
        this.albums.addAll(albums.toAlbumsDtoList())
        refreshFlow()
    }
}