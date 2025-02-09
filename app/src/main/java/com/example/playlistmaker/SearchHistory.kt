package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY_KEY = "search_history_key"

class SearchHistory(context: Context) {
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)

    fun loadHistory(): ArrayList<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }


    private fun saveHistory(history: ArrayList<Track>) {
        val json = Gson().toJson(history)
        sharedPrefs.edit().putString(SEARCH_HISTORY_KEY, json).apply()
    }

    fun addTrack(track: Track) {
        val history = loadHistory()

        history.removeAll { it.trackId == track.trackId }

        history.add(0, track)

        if (history.size > 10) {
            history.removeAt(history.size - 1)
        }

        saveHistory(history)
    }

    fun clearHistory() {
        sharedPrefs.edit().remove(SEARCH_HISTORY_KEY).apply()
    }
}
