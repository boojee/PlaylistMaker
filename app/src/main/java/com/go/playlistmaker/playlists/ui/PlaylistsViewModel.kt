package com.go.playlistmaker.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go.playlistmaker.playlists.domain.api.PlaylistInteractor
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val playlistMutableLiveData = MutableLiveData<List<PlaylistDomain>>()
    val playlistLiveData: LiveData<List<PlaylistDomain>> = playlistMutableLiveData

    init {
        getPlaylists()
    }

    private fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylist()
                .collect {
                    playlistMutableLiveData.postValue(it)
                }
        }
    }
}