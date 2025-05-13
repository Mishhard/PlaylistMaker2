package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SaveSearchTermUseCase
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.player.domain.models.Track

class SaveSearchTermUseCaseImpl(
    private val repo: TrackRepository
) : SaveSearchTermUseCase {
    override fun execute(track: Track) {
        repo.saveSearchTerm(track)
    }
}