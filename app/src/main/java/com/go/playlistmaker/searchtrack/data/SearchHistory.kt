package com.go.playlistmaker.searchtrack.data

import com.go.playlistmaker.searchtrack.domain.models.Track

interface SearchHistory {
    fun addTrack(track: Track)
    fun getHistory(): List<Track>
    fun clearHistory()
}