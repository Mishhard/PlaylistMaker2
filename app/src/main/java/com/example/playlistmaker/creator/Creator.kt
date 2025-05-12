// presentation/Creator.kt
package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.data.repository.SearchHistory
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.api.ClearSearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.GetSearchHistoryInteractor
import com.example.playlistmaker.settings.domain.GetSettingsInteractor
import com.example.playlistmaker.search.domain.api.SaveSearchTermInteractor
import com.example.playlistmaker.settings.domain.SaveSettingsInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.impl.GetSearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.GetSettingsInteractorImpl
import com.example.playlistmaker.search.domain.impl.SaveSearchTermInteractorImpl
import com.example.playlistmaker.domain.impl.SaveSettingsInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.search.domain.impl.ClearSearchHistoryInteractorImpl


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
