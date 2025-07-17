package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.data.dto.PlaylistDto
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.ui.models.TrackUiModel
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<PlaylistDto>()
    val playlist: LiveData<PlaylistDto> get() = _playlist

    private val _tracks = MutableLiveData<List<TrackUiModel>>()
    val tracks: LiveData<List<TrackUiModel>> get() = _tracks

    fun loadPlaylist(id: Long) {
        viewModelScope.launch {
            val playlistDto = playlistInteractor.getPlaylistById(id)
            playlistDto?.let {
                _playlist.postValue(it)

                val trackList = playlistInteractor.getTracksForPlaylist(it.id)
                _tracks.postValue(trackList)
            }
        }
    }

    fun deletePlaylist(id: Long, onDeleted: () -> Unit) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(id)
            onDeleted()
        }
    }

    fun removeTrackFromPlaylist(playlistId: Long, trackId: Int) {
        viewModelScope.launch {
            playlistInteractor.removeTrackFromPlaylist(playlistId, trackId) // новый метод
            val updatedTracks = playlistInteractor.getTracksForPlaylist(playlistId)
            _tracks.postValue(updatedTracks)
        }
    }
}
