package com.go.playlistmaker.audioplayer.ui.playlistbottomsheet

import com.go.playlistmaker.playlists.domain.models.PlaylistDomain

sealed class PlaylistState {
    data class PlaylistList(val playlistList: List<PlaylistDomain>) : PlaylistState()
    data class CheckTrackId(val isContains: Boolean) : PlaylistState()
    data object TrackAdded : PlaylistState()
}