package com.go.playlistmaker.playlistdetails.domain.impl

import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsInteractor
import com.go.playlistmaker.playlistdetails.domain.api.PlaylistDetailsRepository
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain
import kotlinx.coroutines.flow.Flow

class PlaylistDetailsInteractorImpl(private val playlistDetailsRepository: PlaylistDetailsRepository) :
    PlaylistDetailsInteractor {
    override fun getPlaylistById(playlistId: Long): Flow<PlaylistDomain> {
        return playlistDetailsRepository.getPlaylistById(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(
        playlistId: Long,
        trackId: Int
    ): Flow<List<TrackDomain>> {
        return playlistDetailsRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override fun getAllTracksForPlaylist(trackIds: List<Long>): Flow<List<TrackDomain>> {
        return playlistDetailsRepository.getAllTracksForPlaylist(trackIds)
    }

    override suspend fun updatePlaylist(playlist: PlaylistDomain) {
        playlistDetailsRepository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylistAndUnusedTracks(playlistId: Long) {
        playlistDetailsRepository.deletePlaylistAndUnusedTracks(playlistId)
    }
}