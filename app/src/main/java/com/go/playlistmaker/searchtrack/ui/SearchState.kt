package com.go.playlistmaker.searchtrack.ui

import com.go.playlistmaker.searchtrack.domain.models.TrackDomain

sealed class SearchState {
    data class SearchLoading(val isLoading: Boolean): SearchState()
    data class MusicList(val musicList: List<TrackDomain>) : SearchState()
    data class HistoryMusicList(val musicList: List<TrackDomain>) : SearchState()
    data class SearchError(val message: String): SearchState()
}