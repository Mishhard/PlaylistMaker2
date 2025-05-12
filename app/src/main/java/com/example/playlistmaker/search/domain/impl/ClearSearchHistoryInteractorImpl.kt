package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.ClearSearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository

class ClearSearchHistoryInteractorImpl(
    private val repo: TrackRepository
) : ClearSearchHistoryInteractor {
    override fun execute() = repo.clearSearchHistory()
}