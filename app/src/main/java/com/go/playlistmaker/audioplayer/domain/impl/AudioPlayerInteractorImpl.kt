package com.go.playlistmaker.audioplayer.domain.impl

import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerRepository
import java.util.concurrent.Executors

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {
    private var executor = Executors.newCachedThreadPool()

    override fun preparePlayer(previewUrl: String, onCompletion: () -> Unit) {
        audioPlayerRepository.preparePlayer(previewUrl, onCompletion)
    }

    override fun startPlayer(callback: AudioPlayerInteractor.TimeUpdateCallback) {
        executor.execute {
            audioPlayerRepository.startPlayer(object :
                AudioPlayerRepository.TimeUpdateCallback {
                override fun updatePlaybackTime(time: String) {
                    callback.updatePlaybackTime(time)
                }
            })
        }
    }

    override fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }

    override fun release() {
        audioPlayerRepository.release()
    }
}