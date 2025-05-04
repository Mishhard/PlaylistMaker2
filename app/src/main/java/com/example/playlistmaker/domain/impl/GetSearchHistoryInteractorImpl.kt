package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.GetSearchHistoryInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class GetSearchHistoryInteractorImpl(
    private val repo: TrackRepository
) : GetSearchHistoryInteractor {
    override fun execute(): List<Track> = repo.getSearchHistory()
}