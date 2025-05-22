package com.go.playlistmaker.createplaylist.domain.api

import com.go.playlistmaker.playlists.data.db.Playlist

interface CreatePlaylistInteractor {

    suspend fun insertPlaylist(playlist: Playlist)
}