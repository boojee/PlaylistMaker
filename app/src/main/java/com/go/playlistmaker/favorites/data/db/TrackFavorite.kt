package com.go.playlistmaker.favorites.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteTracks")
data class TrackFavorite(
    @PrimaryKey(autoGenerate = true) val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)