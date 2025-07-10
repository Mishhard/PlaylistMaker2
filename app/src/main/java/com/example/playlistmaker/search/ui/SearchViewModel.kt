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
import com.example.playlistmaker.utils.Resource
import com.example.playlistmaker.utils.debounce
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

    private val debounceSearch = debounce<String>(
        delayMillis = 500L,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { term ->
        viewModelScope.launch {
            performSearch(term)
        }
    }

    fun onQueryChanged(text: String) {
        if (text.isBlank()) {
            loadHistory()
        } else {
            debounceSearch(text)
        }
    }

    private fun performSearch(term: String) {
        _state.postValue(SearchState.Loading)
        viewModelScope.launch {
            searchInteractor.execute(term).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        if (resource.data.isEmpty()) {
                            _state.postValue(SearchState.Empty("Ничего не найдено"))
                        } else {
                            _state.postValue(SearchState.Content(resource.data))
                        }
                    }
                    is Resource.Error -> {
                        _state.postValue(SearchState.Error(resource.message ?: "Ошибка сети"))
                    }
                }
            }
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
