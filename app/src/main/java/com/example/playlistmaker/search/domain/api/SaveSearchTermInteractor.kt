package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.Track

interface SaveSearchTermInteractor {
    fun execute(track: Track)
}