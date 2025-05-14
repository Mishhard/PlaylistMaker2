package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.ClearSearchHistoryUseCase
import com.example.playlistmaker.search.domain.api.TrackRepository

class ClearSearchHistoryUseCaseImpl(
    private val repo: TrackRepository
) : ClearSearchHistoryUseCase {
    override fun execute() = repo.clearSearchHistory()
}