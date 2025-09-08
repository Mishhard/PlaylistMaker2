package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.Track

interface SaveSearchTermUseCase {
    fun execute(track: Track)
}