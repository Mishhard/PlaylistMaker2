package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.Track

interface GetSearchHistoryUseCase {
    fun execute(): List<Track>
}