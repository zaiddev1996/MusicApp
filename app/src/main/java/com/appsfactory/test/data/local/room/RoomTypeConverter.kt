package com.appsfactory.test.data.local.room

import androidx.room.TypeConverter
import com.appsfactory.test.data.remote.dto.AlbumResultDto
import com.appsfactory.test.data.remote.dto.ArtistResultDto
import com.appsfactory.test.data.remote.dto.TrackResultDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomTypeConverter {

    private val gson = Gson().apply { serializeNulls() }

    @TypeConverter
    fun fromArtistDto(value: ArtistResultDto.ArtistDto): String {
        return gson.toJson(value, ArtistResultDto.ArtistDto::class.java)
    }

    @TypeConverter
    fun toArtistDto(value: String): ArtistResultDto.ArtistDto {
        return gson.fromJson(value, ArtistResultDto.ArtistDto::class.java)
    }

    @TypeConverter
    fun fromAlbumImageDtoList(value: List<AlbumResultDto.AlbumImageDto>): String {
        val type = object : TypeToken<List<AlbumResultDto.AlbumImageDto>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAlbumImageDtoList(value: String): List<AlbumResultDto.AlbumImageDto> {
        val type = object : TypeToken<List<AlbumResultDto.AlbumImageDto>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromAlbumTrackDtoList(value: List<TrackResultDto.TrackDto>?): String? {
        if (value == null) return null
        val type = object : TypeToken<List<TrackResultDto.TrackDto>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAlbumTrackDtoList(value: String?): List<TrackResultDto.TrackDto> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<TrackResultDto.TrackDto>>() {}.type
        return gson.fromJson(value, type)
    }
}