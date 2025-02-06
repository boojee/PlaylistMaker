package com.go.playlistmaker.searchtrack.data

import com.go.playlistmaker.searchtrack.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}