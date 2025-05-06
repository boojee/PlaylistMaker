package com.go.playlistmaker.favorites.ui

import com.go.playlistmaker.searchtrack.domain.models.Track

sealed class TrackFavoriteState {
    data class TrackFavoriteList(val trackFavoriteList: List<Track>) : TrackFavoriteState()
}