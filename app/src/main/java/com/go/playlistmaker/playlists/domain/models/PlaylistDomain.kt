package com.go.playlistmaker.playlists.domain.models

data class PlaylistDomain(
    val playlistId: Long,
    val playlistName: String,
    val playlistDescription: String,
    val playlistUri: String,
    val playlistTrackIds: List<Int>,
    val playlistTracksCount: Int,
    val playlistTrackTiming: Int
)