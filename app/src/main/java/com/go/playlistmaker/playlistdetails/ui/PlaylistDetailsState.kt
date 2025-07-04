package com.go.playlistmaker.playlistdetails.ui

import com.go.playlistmaker.playlistdetails.data.db.Track
import com.go.playlistmaker.playlists.data.db.Playlist

sealed class PlaylistDetailsState {
    data class PlaylistDetails(val playlist: Playlist) : PlaylistDetailsState()
    data class TracksDetails(val tracks: List<Track>) : PlaylistDetailsState()
}