package com.appsfactory.test.data.mappers

import com.appsfactory.test.data.remote.dto.AlbumResultDto
import com.appsfactory.test.domain.model.album.Album

fun AlbumResultDto.AlbumDto.toAlbum(): Album {
    return Album(
        name = name,
        url = url,
        artist = artistDto.toArtist(),
        imageUrl = images.last().url,
        tracks = tracks?.toTracksList()
    )
}

fun Album.toAlbumDto(): AlbumResultDto.AlbumDto {
    return AlbumResultDto.AlbumDto(
        name = name,
        url = url,
        artistDto = artist.toArtistDto(),
        images = listOf(AlbumResultDto.AlbumImageDto(url = imageUrl)),
        tracks = tracks?.toTracksDtoList()
    )
}

fun AlbumResultDto.toAlbums(): List<Album> {
    return topAlbums.albums.map { it.toAlbum() }
}

fun List<AlbumResultDto.AlbumDto>.toAlbumsList(): List<Album> {
    return map { it.toAlbum() }
}

fun List<Album>.toAlbumsDtoList(): List<AlbumResultDto.AlbumDto> {
    return map { it.toAlbumDto() }
}
