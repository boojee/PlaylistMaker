package com.go.playlistmaker.domain.api

import com.go.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun findMusic(expression: String, consumer: TrackConsumer)
    fun findMusicHistory(consumer: TrackConsumerHistory)
    fun addMusicHistory(track: Track)
    fun clearHistory()

    interface TrackConsumer {
        fun consume(foundMusic: List<Track>)
    }

    interface TrackConsumerHistory {
        fun consume(foundMusic: List<Track>)
    }
}