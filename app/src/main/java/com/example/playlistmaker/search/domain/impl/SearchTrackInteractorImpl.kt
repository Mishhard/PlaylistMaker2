package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.player.domain.models.Track

class SearchTracksInteractorImpl(
    private val repo: TrackRepository
) : SearchTracksInteractor {
    override suspend fun execute(term: String): List<Track> {
        return repo.searchTracks(term)
    }
}