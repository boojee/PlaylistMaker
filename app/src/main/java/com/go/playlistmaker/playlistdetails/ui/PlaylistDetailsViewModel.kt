package com.go.playlistmaker.playlistdetails.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsInteractor
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistDetailsInteractor: PlaylistDetailsInteractor
) : ViewModel() {

    private val playlistDetailsMutableLiveData = MutableLiveData<PlaylistDetailsState>()
    val playlistDetailsLiveData: LiveData<PlaylistDetailsState> = playlistDetailsMutableLiveData

    fun getPlaylistDetailsById(playlistId: Long) {
        viewModelScope.launch {
            playlistDetailsInteractor.getPlaylistById(playlistId)
                .collect { playlist ->
                    playlistDetailsMutableLiveData.postValue(PlaylistDetailsState.PlaylistDetails(playlist))
                }
        }
    }

    fun getAllTracksForPlaylist(trackIds: List<Long>) {
        viewModelScope.launch {
            playlistDetailsInteractor.getAllTracksForPlaylist(trackIds)
                .collect { tracks ->
                    val trackMap = tracks.associateBy { it.trackId }
                    val sortedTracks = trackIds.mapNotNull { trackMap[it] }.reversed()
                    playlistDetailsMutableLiveData.postValue(
                        PlaylistDetailsState.TracksDetails(sortedTracks)
                    )
                }
        }
    }

    fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        viewModelScope.launch {
            playlistDetailsInteractor.deleteTrackFromPlaylist(playlistId, trackId)
                .collect { updatedTracks ->
                    playlistDetailsMutableLiveData.postValue(
                        PlaylistDetailsState.TracksDetails(
                            updatedTracks
                        )
                    )
                }
        }
    }
}