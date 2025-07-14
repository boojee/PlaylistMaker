package com.go.playlistmaker.createplaylist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go.playlistmaker.createplaylist.domain.api.CreatePlaylistInteractor
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsInteractor
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor,
    private val playlistDetailsInteractor: PlaylistDetailsInteractor
) : ViewModel() {

    private val playlistMutableLiveData = MutableLiveData<PlaylistDomain>()
    val playlistLiveData: LiveData<PlaylistDomain> = playlistMutableLiveData

    fun insertPlaylist(playlist: PlaylistDomain) {
        viewModelScope.launch {
            createPlaylistInteractor.insertPlaylist(playlist)
        }
    }

    fun getPlaylistDetailsById(playlistId: Long) {
        viewModelScope.launch {
            playlistDetailsInteractor.getPlaylistById(playlistId)
                .collect{
                    playlistMutableLiveData.postValue(it)
                }
        }
    }

    fun updatePlaylist(playlist: PlaylistDomain) {
        viewModelScope.launch {
            playlistDetailsInteractor.updatePlaylist(playlist)
        }
    }
}