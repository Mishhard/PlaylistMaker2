package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.ClearSearchHistoryInteractor
import com.example.playlistmaker.domain.api.TrackRepository

class ClearSearchHistoryInteractorImpl(
    private val repo: TrackRepository
) : ClearSearchHistoryInteractor {
    override fun execute() = repo.clearSearchHistory()
}