package com.go.playlistmaker.searchtrack.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go.playlistmaker.searchtrack.domain.api.TrackInteractor
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    private val trackInteractor: TrackInteractor
) : ViewModel() {

    private val searchStateLiveData = MutableLiveData<SearchState>()

    init {
        findMusicHistory()
    }

    fun getSearchStateLiveData(): MutableLiveData<SearchState> = searchStateLiveData

    fun findMusic(query: String) {
        searchStateLiveData.postValue(SearchState.SearchLoading(true))

        viewModelScope.launch {
            trackInteractor
                .findMusic(query)
                .collect { pair ->
                    searchStateLiveData.postValue(SearchState.SearchLoading(false))
                    if (pair.second != null) {
                        searchStateLiveData.postValue(SearchState.SearchError(pair.second ?: ""))
                    } else {
                        searchStateLiveData.postValue(SearchState.MusicList(pair.first.orEmpty()))
                    }
                }
        }
    }

    fun addMusicHistory(trackDomain: TrackDomain) {
        viewModelScope.launch { trackInteractor.addMusicHistory(trackDomain) }
    }

    fun findMusicHistory() {
        viewModelScope.launch {
            trackInteractor.findMusicHistory().collect { foundMusic ->
                searchStateLiveData.postValue(SearchState.HistoryMusicList(foundMusic))
            }
        }
    }

    fun clearHistory() {
        trackInteractor.clearHistory()
    }
}