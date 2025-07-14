package com.go.playlistmaker.playlistdetails.ui

import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain

sealed class PlaylistDetailsState {
    data class PlaylistDetails(val playlist: PlaylistDomain) : PlaylistDetailsState()
    data class TracksDetails(val trackDomains: List<TrackDomain>) : PlaylistDetailsState()
}