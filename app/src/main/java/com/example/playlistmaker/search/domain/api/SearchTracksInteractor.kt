package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchTracksInteractor {
    fun execute(term: String): Flow<Resource<List<Track>>>
}