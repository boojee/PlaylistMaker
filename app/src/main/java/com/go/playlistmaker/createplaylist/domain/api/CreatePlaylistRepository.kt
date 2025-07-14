package com.go.playlistmaker.createplaylist.domain.api

import com.go.playlistmaker.playlists.domain.models.PlaylistDomain

interface CreatePlaylistRepository {

    suspend fun insertPlaylist(playlist: PlaylistDomain)
}