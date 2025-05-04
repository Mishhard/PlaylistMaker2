package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SaveSearchTermInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class SaveSearchTermInteractorImpl(
    private val repo: TrackRepository
) : SaveSearchTermInteractor {
    override fun execute(track: Track) {
        repo.saveSearchTerm(track)
    }
}