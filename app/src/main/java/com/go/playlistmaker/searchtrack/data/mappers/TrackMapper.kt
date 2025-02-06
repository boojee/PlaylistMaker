package com.go.playlistmaker.searchtrack.data.mappers

import com.go.playlistmaker.common.utils.TimeUtils.getSimpleDateFormatMmSs
import com.go.playlistmaker.searchtrack.data.dto.TrackDto
import com.go.playlistmaker.searchtrack.domain.models.Track

object TrackMapper {

    fun toTrack(trackDto: TrackDto): Track {
        return Track(
            trackId = trackDto.trackId ?: 0L,
            trackName = trackDto.trackName.orEmpty(),
            artistName = trackDto.artistName.orEmpty(),
            trackTimeMillis = getSimpleDateFormatMmSs(trackDto.trackTimeMillis ?: 0L),
            artworkUrl100 = trackDto.artworkUrl100.orEmpty(),
            collectionName = trackDto.collectionName.orEmpty(),
            releaseDate = trackDto.releaseDate.orEmpty(),
            primaryGenreName = trackDto.primaryGenreName.orEmpty(),
            country = trackDto.country.orEmpty(),
            previewUrl = trackDto.previewUrl.orEmpty()
        )
    }
}