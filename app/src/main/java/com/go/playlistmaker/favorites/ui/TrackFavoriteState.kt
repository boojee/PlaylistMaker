package com.go.playlistmaker.favorites.ui

import com.go.playlistmaker.searchtrack.domain.models.TrackDomain

sealed class TrackFavoriteState {
    data class TrackFavoriteList(val trackDomainFavoriteList: List<TrackDomain>) : TrackFavoriteState()
}