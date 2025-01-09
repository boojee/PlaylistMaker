package com.go.playlistmaker.data.localdata

import android.content.SharedPreferences
import com.go.playlistmaker.common.HISTORY_KEY
import com.go.playlistmaker.common.MAX_HISTORY_SIZE
import com.go.playlistmaker.data.SearchHistory
import com.go.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryImpl(private val sharedPreferences: SharedPreferences) : SearchHistory {

    private val gson = Gson()

    override fun addTrack(track: Track) {
        val history = getHistory().toMutableList()

        history.removeIf { it.trackId == track.trackId }

        history.add(0, track)

        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(history.size - 1)
        }

        sharedPreferences.edit().putString(HISTORY_KEY, serializeHistory(history)).apply()
    }

    override fun getHistory(): List<Track> {
        val historyString = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyList()
        return deserializeHistory(historyString)
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
    }

    private fun serializeHistory(history: List<Track>): String {
        return gson.toJson(history)
    }

    private fun deserializeHistory(historyString: String): List<Track> {
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(historyString, type)
    }
}