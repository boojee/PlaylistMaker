package com.go.playlistmaker.favorites.data.db

import androidx.room.TypeConverter

class PlaylistConverter {
    @TypeConverter
    fun fromTrackIdsList(trackIds: List<Int>): String {
        return trackIds.joinToString(",")
    }

    @TypeConverter
    fun toTrackIdsList(data: String): List<Int> {
        if (data.isEmpty()) return emptyList()
        return data.split(",").map { it.toInt() }
    }
}