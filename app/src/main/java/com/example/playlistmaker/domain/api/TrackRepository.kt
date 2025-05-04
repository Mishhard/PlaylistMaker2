package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    suspend fun searchTracks(term: String): List<Track>
    fun getSearchHistory(): List<Track>
    fun saveSearchTerm(track: Track)
    fun clearSearchHistory()
}