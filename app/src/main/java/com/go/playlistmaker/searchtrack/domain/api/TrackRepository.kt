package com.go.playlistmaker.searchtrack.domain.api

import com.go.playlistmaker.searchtrack.domain.models.Track
import com.go.playlistmaker.searchtrack.util.Resource

interface TrackRepository {
    fun findMusic(expression: String): Resource<List<Track>>
    fun findMusicHistory(): List<Track>
    fun addMusicHistory(track: Track)
    fun clearHistory()
}