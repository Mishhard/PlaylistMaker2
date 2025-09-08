package com.example.playlistmaker.search.ui

import com.example.playlistmaker.player.domain.models.Track

sealed class SearchState {
    object Loading : SearchState()
    data class Content(val tracks: List<Track>) : SearchState()
    data class History(val tracks: List<Track>) : SearchState()
    data class Empty(val message: String) : SearchState()
    data class Error(val message: String) : SearchState()
}