package com.go.playlistmaker.playlists.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class Playlist(
    @PrimaryKey(autoGenerate = true) val playlistId: Long = 0,
    val playlistName: String,
    val playlistDescription: String,
    val playlistUri: String,
    val playlistTrackIds: List<Int>,
    val playlistTracksCount: Int
)