// presentation/Creator.kt
package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.data.repository.SearchHistory
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.api.ClearSearchHistoryUseCase
import com.example.playlistmaker.search.domain.api.GetSearchHistoryUseCase
import com.example.playlistmaker.settings.domain.api.GetSettingsInteractor
import com.example.playlistmaker.search.domain.api.SaveSearchTermUseCase
import com.example.playlistmaker.settings.domain.api.SaveSettingsInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.impl.GetSearchHistoryUseCaseImpl
import com.example.playlistmaker.settings.domain.impl.GetSettingsInteractorImpl
import com.example.playlistmaker.search.domain.impl.SaveSearchTermUseCaseImpl
import com.example.playlistmaker.settings.domain.impl.SaveSettingsInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.search.domain.impl.ClearSearchHistoryUseCaseImpl
import com.example.playlistmaker.player.data.impl.MediaPlayerWrapperImpl
import com.example.playlistmaker.player.data.MediaPlayerWrapper


object Creator {
    private lateinit var app: Context

    fun init(application: Application) {
        app = application
    }

    private val api: ItunesApi by lazy {
        RetrofitClient.instance
    }

    private val historyStorage by lazy {
        SearchHistory(app)
    }

    private val settingsStorage by lazy {
        SettingsRepositoryImpl(app)
    }

    private val trackRepo by lazy {
        TrackRepositoryImpl(api, historyStorage)
    }

    val searchUseCase: SearchTracksInteractor by lazy {
        SearchTracksInteractorImpl(trackRepo)
    }
    val getSearchHistoryInteractor: GetSearchHistoryUseCase by lazy {
        GetSearchHistoryUseCaseImpl(trackRepo)
    }
    val saveSearchTermInteractor: SaveSearchTermUseCase by lazy {
        SaveSearchTermUseCaseImpl(trackRepo)
    }

    val clearSearchHistoryInteractor: ClearSearchHistoryUseCase by lazy {
        ClearSearchHistoryUseCaseImpl(trackRepo)
    }
    val getSettingsInteractor: GetSettingsInteractor by lazy {
        GetSettingsInteractorImpl(settingsStorage)
    }
    val saveSettingsInteractor: SaveSettingsInteractor by lazy {
        SaveSettingsInteractorImpl(settingsStorage)
    }
    private val playerWrapper by lazy { MediaPlayerWrapperImpl() }
    val playerInteractor: PlayerInteractor by lazy {
        PlayerInteractorImpl(playerWrapper)
    }
}
