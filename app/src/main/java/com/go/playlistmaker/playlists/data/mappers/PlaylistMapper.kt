package com.go.playlistmaker.playlists.data.mappers

import com.go.playlistmaker.playlists.data.db.Playlist
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain

object PlaylistMapper {
    fun toPlaylist(playlist: Playlist): PlaylistDomain {
        return PlaylistDomain(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            playlistUri = playlist.playlistUri,
            playlistTrackIds = playlist.playlistTrackIds,
            playlistTracksCount = playlist.playlistTracksCount,
            playlistTrackTiming = playlist.playlistTrackTiming
        )
    }
}