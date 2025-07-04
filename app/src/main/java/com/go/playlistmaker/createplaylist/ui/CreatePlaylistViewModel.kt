package com.go.playlistmaker.createplaylist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go.playlistmaker.createplaylist.domain.api.CreatePlaylistInteractor
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsInteractor
import com.go.playlistmaker.playlists.data.db.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor,
    private val playlistDetailsInteractor: PlaylistDetailsInteractor
) : ViewModel() {

    private val playlistMutableLiveData = MutableLiveData<Playlist>()
    val playlistLiveData: LiveData<Playlist> = playlistMutableLiveData

    fun insertPlaylist(playlist: Playlist) {
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

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistDetailsInteractor.updatePlaylist(playlist)
        }
    }
}