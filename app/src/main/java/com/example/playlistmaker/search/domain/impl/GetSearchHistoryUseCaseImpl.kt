package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.GetSearchHistoryUseCase
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.player.domain.models.Track

class GetSearchHistoryUseCaseImpl(
    private val repo: TrackRepository
) : GetSearchHistoryUseCase {
    override fun execute(): List<Track> = repo.getSearchHistory()
}