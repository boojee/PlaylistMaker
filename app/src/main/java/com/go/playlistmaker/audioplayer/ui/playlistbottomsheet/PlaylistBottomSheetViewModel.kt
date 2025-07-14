package com.go.playlistmaker.audioplayer.ui.playlistbottomsheet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import kotlinx.coroutines.launch

class PlaylistBottomSheetViewModel(private val audioPlayerInteractor: AudioPlayerInteractor): ViewModel() {

    private val playlistStateLiveData = MutableLiveData<PlaylistState>()

    init {
        getPlaylist()
    }

    fun getPlaylistStateLiveData(): MutableLiveData<PlaylistState> = playlistStateLiveData

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

    fun insertTrack(playlistId: Long, track: TrackDomain) {
        viewModelScope.launch {
            audioPlayerInteractor.insertTrack(track)
            audioPlayerInteractor.updatePlaylistTrackIds(playlistId, track.trackId.toInt())
            playlistStateLiveData.postValue(PlaylistState.TrackAdded)
        }
    }
}