package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.common.App
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.ui.models.TrackUiModel
import kotlinx.coroutines.launch
import com.example.playlistmaker.library.data.dto.toDomain
import com.example.playlistmaker.utils.SingleLiveEvent

class PlaylistDetailsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> get() = _playlist


    private val _tracks = MutableLiveData<List<TrackUiModel>>()
    val tracks: LiveData<List<TrackUiModel>> get() = _tracks

    val shareEvent = SingleLiveEvent<String>()
    val errorEvent = SingleLiveEvent<Int>()

    fun loadPlaylist(id: Long) {
        viewModelScope.launch {
            val dto = playlistInteractor.getPlaylistById(id)
            val domainPlaylist = dto?.toDomain()
            _playlist.postValue(domainPlaylist)

            if (domainPlaylist != null) {
                val loadedTracks = playlistInteractor.getTracksForPlaylist(id)
                _tracks.postValue(loadedTracks)
            } else {
                _tracks.postValue(emptyList())
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
            playlistInteractor.removeTrackFromPlaylist(playlistId, trackId)
            val updatedTracks = playlistInteractor.getTracksForPlaylist(playlistId)
            _tracks.postValue(updatedTracks)
        }
    }

    fun sharePlaylist() {
        val playlist = _playlist.value
        val tracks = _tracks.value ?: emptyList()

        if (playlist == null || tracks.isEmpty()) {
            errorEvent.postValue(R.string.empty_playlist_error)
            return
        }

        val shareText = buildString {
            append(playlist.name)
            if (!playlist.description.isNullOrBlank()) append("\n${playlist.description}")
            append("\n\n")

            val trackCountText = App.instance.resources.getQuantityString(
                R.plurals.track_count,
                tracks.size,
                tracks.size
            )

            append("$trackCountText:\n\n")
            tracks.forEachIndexed { index, track ->
                append("${index + 1}. ${track.artistName} - ${track.trackName} (${track.getFormattedTime()})\n")
            }
        }
        shareEvent.postValue(shareText)
    }
}

