package com.go.playlistmaker.audioplayer.domain.api

interface AudioPlayerRepository {
    fun preparePlayer(previewUrl: String, onCompletion: () -> Unit)
    fun startPlayer(callback: TimeUpdateCallback)
    fun pausePlayer()
    fun release()

    interface TimeUpdateCallback {
        fun updatePlaybackTime(time: String)
    }
}