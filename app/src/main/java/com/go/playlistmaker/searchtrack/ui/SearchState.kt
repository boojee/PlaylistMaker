package com.go.playlistmaker.searchtrack.ui

import com.go.playlistmaker.searchtrack.domain.models.Track

sealed class SearchState {
    data class SearchLoading(val isLoading: Boolean): SearchState()
    data class MusicList(val musicList: List<Track>) : SearchState()
    data class HistoryMusicList(val musicList: List<Track>) : SearchState()
    data class SearchError(val message: String): SearchState()
}