package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import com.example.playlistmaker.utils.Resource

class SearchTracksInteractorImpl(
    private val repo: TrackRepository
) : SearchTracksInteractor {
    override fun execute(term: String): Flow<Resource<List<Track>>> {
        return repo.searchTracks(term)
    }

}