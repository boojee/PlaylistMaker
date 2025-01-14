package com.go.playlistmaker.data

import com.go.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}