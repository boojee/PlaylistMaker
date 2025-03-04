package com.go.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerRepositoryImpl(
    private var mediaPlayer: MediaPlayer,
    private var scope: CoroutineScope
) : AudioPlayerRepository {

    private var timerJob: Job? = null
    private var isUpdatingTime = false

    override fun preparePlayer(previewUrl: String, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onCompletion()
        }
        mediaPlayer.setOnCompletionListener {
            stopUpdatingTime()
            onCompletion()
        }
    }

    override fun startPlayer(callback: AudioPlayerRepository.TimeUpdateCallback) {
        mediaPlayer.start()
        startUpdatingTime(callback)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        stopUpdatingTime()
    }

    override fun release() {
        mediaPlayer.release()
    }

    private fun startUpdatingTime(callback: AudioPlayerRepository.TimeUpdateCallback) {
        if (isUpdatingTime) return
        isUpdatingTime = true

        timerJob = scope.launch {
            while (mediaPlayer.isPlaying) {
                delay(300L)
                val currentPosition = mediaPlayer.currentPosition
                val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(currentPosition)
                callback.updatePlaybackTime(formattedTime)
            }
        }
    }

    private fun stopUpdatingTime() {
        isUpdatingTime = false
        timerJob?.cancel()
    }
}