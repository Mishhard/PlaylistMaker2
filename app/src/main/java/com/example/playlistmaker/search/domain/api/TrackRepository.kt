package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.Track

interface TrackRepository {
    suspend fun searchTracks(term: String): List<Track>
    fun getSearchHistory(): List<Track>
    fun saveSearchTerm(track: Track)
    fun clearSearchHistory()
}