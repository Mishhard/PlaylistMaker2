package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import com.example.playlistmaker.utils.Resource

interface TrackRepository {
    fun searchTracks(term: String): Flow<Resource<List<Track>>>
    fun getSearchHistory(): List<Track>
    fun saveSearchTerm(track: Track)
    fun clearSearchHistory()
}