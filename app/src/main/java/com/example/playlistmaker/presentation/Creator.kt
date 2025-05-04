// presentation/Creator.kt
package com.example.playlistmaker.presentation

import android.content.Context
import com.example.playlistmaker.data.network.ItunesApi
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.data.storage.SettingsRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.ClearSearchHistoryInteractor
import com.example.playlistmaker.domain.api.GetSearchHistoryInteractor
import com.example.playlistmaker.domain.api.GetSettingsInteractor
import com.example.playlistmaker.domain.api.SaveSearchTermInteractor
import com.example.playlistmaker.domain.api.SaveSettingsInteractor
import com.example.playlistmaker.domain.api.SearchTracksInteractor
import com.example.playlistmaker.domain.impl.GetSearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.GetSettingsInteractorImpl
import com.example.playlistmaker.domain.impl.SaveSearchTermInteractorImpl
import com.example.playlistmaker.domain.impl.SaveSettingsInteractorImpl
import com.example.playlistmaker.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.domain.impl.ClearSearchHistoryInteractorImpl


object Creator {
    private lateinit var context: Context

    fun init(ctx: Context) {
        context = ctx.applicationContext
    }

    private val api: ItunesApi by lazy {
        RetrofitClient.instance
    }

    private val historyStorage by lazy {
        SearchHistory(context)
    }

    private val settingsStorage by lazy {
        SettingsRepositoryImpl(context)
    }

    private val trackRepo by lazy {
        TrackRepositoryImpl(api, historyStorage)
    }

    // Use-cases for tracks
    val searchUseCase: SearchTracksInteractor by lazy {
        SearchTracksInteractorImpl(trackRepo)
    }
    val getSearchHistoryInteractor: GetSearchHistoryInteractor by lazy {
        GetSearchHistoryInteractorImpl(trackRepo)
    }
    val saveSearchTermInteractor: SaveSearchTermInteractor by lazy {
        SaveSearchTermInteractorImpl(trackRepo)
    }

    val clearSearchHistoryInteractor: ClearSearchHistoryInteractor by lazy {
        ClearSearchHistoryInteractorImpl(trackRepo)
    }
    // Use-cases for settings
    val getSettingsInteractor: GetSettingsInteractor by lazy {
        GetSettingsInteractorImpl(settingsStorage)
    }
    val saveSettingsInteractor: SaveSettingsInteractor by lazy {
        SaveSettingsInteractorImpl(settingsStorage)
    }
}
