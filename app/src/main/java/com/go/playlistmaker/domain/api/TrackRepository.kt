package com.go.playlistmaker.domain.api

import com.go.playlistmaker.domain.models.Track

interface TrackRepository {
    fun findMusic(expression: String): List<Track>
    fun findMusicHistory(): List<Track>
    fun addMusicHistory(track: Track)
    fun clearHistory()
}