// SearchViewModel.kt
package com.example.playlistmaker.search.ui

import androidx.lifecycle.*
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.domain.api.ClearSearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.GetSearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SaveSearchTermInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchTracksInteractor,
    private val historyInteractor: GetSearchHistoryInteractor,
    private val saveTermInteractor: SaveSearchTermInteractor,
    private val clearHistoryInteractor: ClearSearchHistoryInteractor
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

    companion object {
        fun provideFactory(
            searchInteractor: SearchTracksInteractor,
            historyInteractor: GetSearchHistoryInteractor,
            saveTermInteractor: SaveSearchTermInteractor,
            clearHistoryInteractor: ClearSearchHistoryInteractor
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(
                    searchInteractor,
                    historyInteractor,
                    saveTermInteractor,
                    clearHistoryInteractor
                ) as T
            }
        }
    }
}
