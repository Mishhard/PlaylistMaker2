package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.Track

interface SearchTracksInteractor {
    suspend fun execute(term: String): List<Track>
}