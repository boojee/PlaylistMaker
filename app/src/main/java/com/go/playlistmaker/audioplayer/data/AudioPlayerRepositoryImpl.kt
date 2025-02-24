package com.go.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : AudioPlayerRepository {

    private val handler = Handler(Looper.getMainLooper())
    private var updateRunnable: Runnable? = null
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
        stopUpdatingTime()
    }

    override fun release() {
        mediaPlayer.release()
    }

    private fun startUpdatingTime(callback: AudioPlayerRepository.TimeUpdateCallback) {
        if (isUpdatingTime) return
        isUpdatingTime = true

        updateRunnable = object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    val currentPosition = mediaPlayer.currentPosition
                    val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(currentPosition)
                    callback.updatePlaybackTime(formattedTime)
                }

                if (isUpdatingTime) {
                    handler.postDelayed(this, 500L)
                }
            }
        }
        updateRunnable?.let { handler.post(it) }
    }

    private fun stopUpdatingTime() {
        isUpdatingTime = false
        updateRunnable?.let { handler.removeCallbacks(it) }
        updateRunnable = null
    }
}