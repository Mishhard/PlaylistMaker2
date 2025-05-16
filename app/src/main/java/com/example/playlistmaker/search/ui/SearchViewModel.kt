// SearchViewModel.kt
package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.domain.api.ClearSearchHistoryUseCase
import com.example.playlistmaker.search.domain.api.GetSearchHistoryUseCase
import com.example.playlistmaker.search.domain.api.SaveSearchTermUseCase
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchTracksInteractor,
    private val historyInteractor: GetSearchHistoryUseCase,
    private val saveTermInteractor: SaveSearchTermUseCase,
    private val clearHistoryInteractor: ClearSearchHistoryUseCase
) : ViewModel() {

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state
    private var searchJob: Job? = null

    init {
        loadHistory()
    }

    fun onQueryChanged(text: String) {
        searchJob?.cancel()
        if (text.isBlank()) {
            loadHistory()
        } else {
            searchJob = viewModelScope.launch {
                delay(500)
                performSearch(text)
            }
        }
    }

    private suspend fun performSearch(term: String) {
        _state.postValue(SearchState.Loading)
        try {
            val results = searchInteractor.execute(term)
            if (results.isEmpty()) {
                _state.postValue(SearchState.Empty("Ничего не найдено"))
            } else {
                _state.postValue(SearchState.Content(results))
            }
        } catch (e: Throwable) {
            _state.postValue(SearchState.Error("Ошибка сети"))
        }
    }

    fun onTrackClicked(track: Track) {
        saveTermInteractor.execute(track)
        val history = historyInteractor.execute()
        _state.value = SearchState.History(history)
    }

    fun saveTrack(track: Track) {
        saveTermInteractor.execute(track)
    }

    fun loadHistory() {
        val history = historyInteractor.execute()
        _state.value = SearchState.History(history)
    }

    fun clearHistory() {
        clearHistoryInteractor.execute()
        _state.value = SearchState.History(emptyList())
    }
}
