package com.go.playlistmaker.audioplayer.ui.playlistbottomsheet

import com.go.playlistmaker.playlists.data.db.Playlist

sealed class PlaylistState {
    data class PlaylistList(val playlistList: List<Playlist>) : PlaylistState()
    data class CheckTrackId(val isContains: Boolean) : PlaylistState()
}