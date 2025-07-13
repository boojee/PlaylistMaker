package com.go.playlistmaker.searchtrack.data.mappers

import com.go.playlistmaker.common.utils.TimeUtils.getSimpleDateFormatMmSs
import com.go.playlistmaker.favorites.data.db.TrackFavorite
import com.go.playlistmaker.playlistdetails.data.db.Track
import com.go.playlistmaker.searchtrack.data.dto.TrackDto
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain

object TrackMapper {

    fun toTrack(trackDto: TrackDto): TrackDomain {
        return TrackDomain(
            trackId = trackDto.trackId ?: 0L,
            trackName = trackDto.trackName.orEmpty(),
            artistName = trackDto.artistName.orEmpty(),
            trackTimeMillis = getSimpleDateFormatMmSs(trackDto.trackTimeMillis ?: 0L),
            artworkUrl100 = trackDto.artworkUrl100.orEmpty(),
            collectionName = trackDto.collectionName.orEmpty(),
            releaseDate = trackDto.releaseDate.orEmpty(),
            primaryGenreName = trackDto.primaryGenreName.orEmpty(),
            country = trackDto.country.orEmpty(),
            previewUrl = trackDto.previewUrl.orEmpty(),
            isFavorite = trackDto.isFavorite
        )
    }

    fun toTrack(track: Track): TrackDomain {
        return TrackDomain(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = track.isFavorite
        )
    }

    fun toTrackEntity(trackDomain: TrackDomain): Track {
        return Track(
            trackId = trackDomain.trackId,
            trackName = trackDomain.trackName,
            artistName = trackDomain.artistName,
            trackTimeMillis = trackDomain.trackTimeMillis,
            artworkUrl100 = trackDomain.artworkUrl100,
            collectionName = trackDomain.collectionName,
            releaseDate = trackDomain.releaseDate,
            primaryGenreName = trackDomain.primaryGenreName,
            country = trackDomain.country,
            previewUrl = trackDomain.previewUrl,
            isFavorite = trackDomain.isFavorite
        )
    }

    fun trackFavoriteToTrack(trackFavorite: TrackFavorite): TrackDomain {
        return TrackDomain(
            trackId = trackFavorite.trackId,
            trackName = trackFavorite.trackName,
            artistName = trackFavorite.artistName,
            trackTimeMillis = trackFavorite.trackTimeMillis,
            artworkUrl100 = trackFavorite.artworkUrl100,
            collectionName = trackFavorite.collectionName,
            releaseDate = trackFavorite.releaseDate,
            primaryGenreName = trackFavorite.primaryGenreName,
            country = trackFavorite.country,
            previewUrl = trackFavorite.previewUrl,
            isFavorite = trackFavorite.isFavorite
        )
    }

    fun trackFavoriteToTrack(trackDomain: TrackDomain): TrackFavorite {
        return TrackFavorite(
            trackId = trackDomain.trackId,
            trackName = trackDomain.trackName,
            artistName = trackDomain.artistName,
            trackTimeMillis = trackDomain.trackTimeMillis,
            artworkUrl100 = trackDomain.artworkUrl100,
            collectionName = trackDomain.collectionName,
            releaseDate = trackDomain.releaseDate,
            primaryGenreName = trackDomain.primaryGenreName,
            country = trackDomain.country,
            previewUrl = trackDomain.previewUrl,
            isFavorite = trackDomain.isFavorite
        )
    }
}