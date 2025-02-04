package com.go.playlistmaker.searchtrack.data.dto

data class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()