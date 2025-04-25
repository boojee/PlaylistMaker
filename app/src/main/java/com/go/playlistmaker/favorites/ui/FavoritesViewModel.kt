package com.go.playlistmaker.favorites.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go.playlistmaker.favorites.domain.api.TrackFavoriteInteractor
import kotlinx.coroutines.launch

class FavoritesViewModel(private val trackFavoriteInteractor: TrackFavoriteInteractor) :
    ViewModel() {

    private val trackFavoriteStateLiveData = MutableLiveData<TrackFavoriteState>()

    init {
        findTrackFavorites()
    }

    fun getTrackFavoriteStateLiveData(): MutableLiveData<TrackFavoriteState> =
        trackFavoriteStateLiveData

    private fun findTrackFavorites() {
        viewModelScope.launch {
            trackFavoriteInteractor.getAllItem().collect {
                trackFavoriteStateLiveData.postValue(TrackFavoriteState.TrackFavoriteList(it))
            }
        }
    }
}