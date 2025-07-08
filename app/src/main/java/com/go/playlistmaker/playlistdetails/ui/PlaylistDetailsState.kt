package com.go.playlistmaker.playlistdetails.ui

import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import com.go.playlistmaker.playlists.data.db.Playlist

sealed class PlaylistDetailsState {
    data class PlaylistDetails(val playlist: Playlist) : PlaylistDetailsState()
    data class TracksDetails(val trackDomains: List<TrackDomain>) : PlaylistDetailsState()
}