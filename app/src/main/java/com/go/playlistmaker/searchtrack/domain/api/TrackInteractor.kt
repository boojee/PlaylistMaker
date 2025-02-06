package com.go.playlistmaker.searchtrack.domain.api

import com.go.playlistmaker.searchtrack.domain.models.Track

interface TrackInteractor {
    fun findMusic(expression: String, consumer: TrackConsumer)
    fun findMusicHistory(consumer: TrackConsumerHistory)
    fun addMusicHistory(track: Track)
    fun clearHistory()

    interface TrackConsumer {
        fun consume(foundMusic: List<Track>, errorMessage: String?)
    }

    interface TrackConsumerHistory {
        fun consume(foundMusic: List<Track>)
    }
}