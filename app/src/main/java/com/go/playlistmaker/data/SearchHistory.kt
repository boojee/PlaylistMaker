package com.go.playlistmaker.data

import com.go.playlistmaker.domain.models.Track

interface SearchHistory {
    fun addTrack(track: Track)
    fun getHistory(): List<Track>
    fun clearHistory()
}