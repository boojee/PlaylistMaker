package com.go.playlistmaker.searchtrack.data.localdata

import android.content.SharedPreferences
import com.go.playlistmaker.searchtrack.data.SearchHistory
import com.go.playlistmaker.searchtrack.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryImpl(private val sharedPreferences: SharedPreferences) : SearchHistory {

    companion object {
        private const val HISTORY_KEY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }

    private val gson = Gson()

    override suspend fun addTrack(track: Track) {
        getHistory().collect { history ->
            val mutableHistoryList = history.toMutableList()
            mutableHistoryList.removeIf { it.trackId == track.trackId }

            mutableHistoryList.add(0, track)

            if (mutableHistoryList.size > MAX_HISTORY_SIZE) {
                mutableHistoryList.removeAt(history.size - 1)
            }

            sharedPreferences.edit().putString(HISTORY_KEY, serializeHistory(mutableHistoryList))
                .apply()
        }
    }

    override fun getHistory(): Flow<List<Track>> = flow {
        val historyString = sharedPreferences.getString(HISTORY_KEY, null)
        emit(deserializeHistory(historyString.orEmpty()))
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