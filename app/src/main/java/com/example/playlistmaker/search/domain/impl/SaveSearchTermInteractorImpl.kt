package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SaveSearchTermInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class SaveSearchTermInteractorImpl(
    private val repo: TrackRepository
) : SaveSearchTermInteractor {
    override fun execute(track: Track) {
        repo.saveSearchTerm(track)
    }
}