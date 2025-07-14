package com.go.playlistmaker.searchtrack.data.localdata

import android.content.SharedPreferences
import com.go.playlistmaker.searchtrack.data.SearchHistory
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class SearchHistoryImpl(private val sharedPreferences: SharedPreferences) : SearchHistory {

    companion object {
        private const val HISTORY_KEY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }

    private val gson = Gson()

    override suspend fun addTrack(trackDomain: TrackDomain) {
        val historyList = getHistory().firstOrNull() ?: emptyList()

        val mutableHistoryList = historyList.toMutableList()
        mutableHistoryList.removeIf { it.trackId == trackDomain.trackId }

        mutableHistoryList.add(0, trackDomain)

        if (mutableHistoryList.size > MAX_HISTORY_SIZE) {
            mutableHistoryList.removeAt(mutableHistoryList.size - 1)
        }

        sharedPreferences.edit()
            .putString(HISTORY_KEY, serializeHistory(mutableHistoryList))
            .apply()
    }

    override fun getHistory(): Flow<List<TrackDomain>> = flow {
        val historyString = sharedPreferences.getString(HISTORY_KEY, null)
        if (historyString != null) {
            emit(deserializeHistory(historyString))
        }
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
    }

    private fun serializeHistory(history: List<TrackDomain>): String {
        return gson.toJson(history)
    }

    private fun deserializeHistory(historyString: String): List<TrackDomain> {
        val type = object : TypeToken<List<TrackDomain>>() {}.type
        return gson.fromJson(historyString, type)
    }
}