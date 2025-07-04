package com.go.playlistmaker.playlistdetails.domain.impl

import com.go.playlistmaker.playlistdetails.data.db.Track
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsInteractor
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsRepository
import com.go.playlistmaker.playlists.data.db.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistDetailsInteractorImpl(private val playlistDetailsRepository: PlaylistDetailsRepository) :
    PlaylistDetailsInteractor {
    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistDetailsRepository.getPlaylistById(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(
        playlistId: Long,
        trackId: Int
    ): Flow<List<Track>> {
        return playlistDetailsRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override fun getAllTracksForPlaylist(trackIds: List<Long>): Flow<List<Track>> {
        return playlistDetailsRepository.getAllTracksForPlaylist(trackIds)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDetailsRepository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylistAndUnusedTracks(playlistId: Long) {
        playlistDetailsRepository.deletePlaylistAndUnusedTracks(playlistId)
    }
}