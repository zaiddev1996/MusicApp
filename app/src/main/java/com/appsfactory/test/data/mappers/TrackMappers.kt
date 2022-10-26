package com.appsfactory.test.data.mappers

import android.text.format.DateUtils
import com.appsfactory.test.data.remote.dto.TrackResultDto
import com.appsfactory.test.domain.model.track.Track

fun TrackResultDto.TrackDto.toTrack(): Track {
    return Track(
        name = name,
        url = url,
        artist = artistDto.toArtist(),
        duration = DateUtils.formatElapsedTime(duration ?: 0L)
    )
}

fun Track.toTrackDto(): TrackResultDto.TrackDto {
    val time = duration.split(":")
    val duration = (time.first().toInt() * 60) + time.last().toInt()
    return TrackResultDto.TrackDto(
        name = name,
        url = url,
        artistDto = artist.toArtistDto(),
        duration = duration.toLong()
    )
}

fun TrackResultDto.toTracks(): List<Track> {
    return album.tracksInfo?.tracks?.map {
        it.toTrack()
    } ?: emptyList()
}

fun List<TrackResultDto.TrackDto>.toTracksList(): List<Track> {
    return map {
        it.toTrack()
    }
}

fun List<Track>.toTracksDtoList(): List<TrackResultDto.TrackDto> {
    return map {
        it.toTrackDto()
    }
}
