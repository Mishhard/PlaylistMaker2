package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.GetSearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.player.domain.models.Track

class GetSearchHistoryInteractorImpl(
    private val repo: TrackRepository
) : GetSearchHistoryInteractor {
    override fun execute(): List<Track> = repo.getSearchHistory()
}