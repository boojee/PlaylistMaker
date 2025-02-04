package com.go.playlistmaker.audioplayer.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.go.playlistmaker.App
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor

class AudioPlayerViewModel(private val audioPlayerInteractor: AudioPlayerInteractor) : ViewModel() {

    private val trackStateLiveData = MutableLiveData<TrackState>()
    private var playerState = STATE_DEFAULT

    init {
        playbackControl()
    }

    fun getTrackStateLiveData(): MutableLiveData<TrackState> = trackStateLiveData


    fun preparePlayer(previewUrl: String) {
        audioPlayerInteractor.preparePlayer(previewUrl) {
            trackStateLiveData.postValue(TrackState.Prepared)
            playerState = STATE_PREPARED
        }
    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
    }

    fun release() {
        audioPlayerInteractor.release()
    }


    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                playerState = STATE_PAUSED
                audioPlayerInteractor.pausePlayer()
                trackStateLiveData.postValue(TrackState.Paused)
            }

            STATE_PREPARED, STATE_PAUSED -> {
                playerState = STATE_PLAYING
                audioPlayerInteractor.startPlayer(object :
                    AudioPlayerInteractor.TimeUpdateCallback {
                    override fun updatePlaybackTime(time: String) {
                        trackStateLiveData.postValue(TrackState.Playing(time))
                    }
                })
                trackStateLiveData.postValue(TrackState.Playing(DEFAULT_TRACK_TIME))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DEFAULT_TRACK_TIME = "00:00"

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val audioPlayerInteractor =
                    (this[APPLICATION_KEY] as App).provideAudioPlayerInteractor()
                AudioPlayerViewModel(audioPlayerInteractor)
            }
        }
    }
}