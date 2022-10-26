package com.appsfactory.test.data.local.room

import androidx.room.*
import com.appsfactory.test.data.remote.dto.AlbumResultDto
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: AlbumResultDto.AlbumDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumResultDto.AlbumDto>)

    @Delete
    suspend fun deleteAlbum(album: AlbumResultDto.AlbumDto)

    @Query("SELECT * FROM AlbumDto")
    fun getAllAlbums(): Flow<List<AlbumResultDto.AlbumDto>>

    @Query("SELECT EXISTS(SELECT * FROM AlbumDto WHERE name = :name)")
    suspend fun isExists(name: String): Boolean

    @Query("SELECT * FROM AlbumDto WHERE name = :name")
    suspend fun getAlbum(name: String): AlbumResultDto.AlbumDto?

    @Query("DELETE FROM AlbumDto")
    suspend fun deleteAllAlbums()
}