package com.go.playlistmaker.playlistdetails.ui.playlistoptionsbottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsInteractor
import kotlinx.coroutines.launch

class PlaylistOptionsBottomSheetViewModel(
    private val playlistDetailsInteractor: PlaylistDetailsInteractor
) : ViewModel() {

    fun deletePlaylistAndUnusedTracks(playlistId: Long) {
        viewModelScope.launch {
            playlistDetailsInteractor.deletePlaylistAndUnusedTracks(playlistId)
        }
    }
}