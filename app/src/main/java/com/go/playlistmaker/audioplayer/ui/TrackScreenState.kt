package com.go.playlistmaker.audioplayer.ui

sealed class TrackState {
    object Prepared : TrackState()
    object Paused : TrackState()
    data class Playing(val playbackTime: String) : TrackState()
}