package com.go.playlistmaker.audioplayer.ui.playlistbottomsheet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import kotlinx.coroutines.launch

class PlaylistBottomSheetViewModel(private val audioPlayerInteractor: AudioPlayerInteractor): ViewModel() {

    private val playlistStateLiveData = MutableLiveData<PlaylistState>()

    init {
        getPlaylist()
    }

    fun getPlaylistStateLiveData(): MutableLiveData<PlaylistState> = playlistStateLiveData

    fun updatePlaylistTrackIds(playlistId: Long, trackId: Int){
        viewModelScope.launch {
            audioPlayerInteractor.updatePlaylistTrackIds(playlistId, trackId)
        }
    }

    fun checkContainsTrackIdInPlaylist(playlistId: Long, trackId: Long){
        viewModelScope.launch {
            audioPlayerInteractor.checkContainsTrackIdInPlaylist(playlistId, trackId)
                .collect{
                    playlistStateLiveData.postValue(PlaylistState.CheckTrackId(it))
                }
        }
    }

    fun getPlaylist() {
        viewModelScope.launch {
            audioPlayerInteractor.getPlaylist()
                .collect {
                    playlistStateLiveData.postValue(PlaylistState.PlaylistList(it))
                }
        }
    }
}