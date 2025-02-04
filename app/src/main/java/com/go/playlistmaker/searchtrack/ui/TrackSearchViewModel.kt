package com.go.playlistmaker.searchtrack.ui

import androidx.lifecycle.LiveData
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

    private var loadingLiveData = MutableLiveData(false)
    private var findMusicLiveData = MutableLiveData<List<Track>>()
    private var findMusicHistoryLiveData = MutableLiveData<List<Track>>()
    private var errorMessageLiveData = MutableLiveData<String>()

    init {
        findMusicHistory()
    }

    fun getLoadingLiveData(): LiveData<Boolean> = loadingLiveData
    fun getFoundMusicLiveData(): LiveData<List<Track>> = findMusicLiveData
    fun getFoundMusicHistoryLiveData(): LiveData<List<Track>> = findMusicHistoryLiveData
    fun getErrorMessage(): LiveData<String> = errorMessageLiveData

    fun findMusic(query: String) {
        loadingLiveData.postValue(true)
        trackInteractor.findMusic(
            query,
            object : TrackInteractor.TrackConsumer {
                override fun consume(foundMusic: List<Track>, errorMessage: String?) {
                    loadingLiveData.postValue(false)
                    if (errorMessage != null) {
                        errorMessageLiveData.postValue(errorMessage.orEmpty())
                    } else {
                        findMusicLiveData.postValue(foundMusic)
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
                findMusicHistoryLiveData.postValue(foundMusic)
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