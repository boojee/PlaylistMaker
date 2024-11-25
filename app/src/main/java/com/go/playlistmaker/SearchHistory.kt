package com.go.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val HISTORY_KEY = "search_history"
private const val MAX_HISTORY_SIZE = 10

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson()

    fun addTrack(track: Track) {
        val history = getHistory().toMutableList()

        history.removeIf { it.trackId == track.trackId }

        history.add(0, track)

        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(history.size - 1)
        }

        sharedPreferences.edit().putString(HISTORY_KEY, serializeHistory(history)).apply()
    }

    fun getHistory(): List<Track> {
        val historyString = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyList()
        return deserializeHistory(historyString)
    }

    fun clearHistory() {
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