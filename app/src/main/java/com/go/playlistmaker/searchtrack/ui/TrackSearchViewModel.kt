package com.go.playlistmaker.searchtrack.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.go.playlistmaker.App
import com.go.playlistmaker.searchtrack.domain.api.TrackInteractor
import com.go.playlistmaker.searchtrack.domain.models.Track

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
        trackInteractor.findMusic(
            query,
            object : TrackInteractor.TrackConsumer {
                override fun consume(foundMusic: List<Track>, errorMessage: String?) {
                    searchStateLiveData.postValue(SearchState.SearchLoading(false))
                    if (errorMessage != null) {
                        searchStateLiveData.postValue(SearchState.SearchError(errorMessage))
                    } else {
                        searchStateLiveData.postValue(SearchState.MusicList(foundMusic))
                    }
                }
            })
    }

    fun addMusicHistory(track: Track) {
        trackInteractor.addMusicHistory(track)
    }

    fun findMusicHistory() {
        trackInteractor.findMusicHistory(object :
            TrackInteractor.TrackConsumerHistory {
            override fun consume(foundMusic: List<Track>) {
                searchStateLiveData.postValue(SearchState.HistoryMusicList(foundMusic))
            }
        })
    }

    fun clearHistory() {
        trackInteractor.clearHistory()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = (this[APPLICATION_KEY] as App).provideTrackInteractor()

                TrackSearchViewModel(
                    interactor
                )
            }
        }
    }
}