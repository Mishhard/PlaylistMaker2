package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchTracksInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class SearchTracksInteractorImpl(
    private val repo: TrackRepository
) : SearchTracksInteractor {
    override suspend fun execute(term: String): List<Track> {
        return repo.searchTracks(term)
    }
}