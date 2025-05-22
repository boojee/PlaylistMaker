package com.go.playlistmaker.audioplayer.domain.impl

import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.go.playlistmaker.audioplayer.domain.api.AudioPlayerRepository
import com.go.playlistmaker.playlists.data.db.Playlist
import kotlinx.coroutines.flow.Flow
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

    override suspend fun updatePlaylistTrackIds(playlistId: Long, trackId: Int) {
        audioPlayerRepository.updatePlaylistTrackIds(playlistId, trackId)
    }

    override suspend fun checkContainsTrackIdInPlaylist(
        playlistId: Long,
        trackId: Long
    ): Flow<Boolean> =
        audioPlayerRepository.checkContainsTrackIdInPlaylist(playlistId, trackId)

    override fun getPlaylist(): Flow<List<Playlist>> {
        return audioPlayerRepository.getPlaylist()
    }
}