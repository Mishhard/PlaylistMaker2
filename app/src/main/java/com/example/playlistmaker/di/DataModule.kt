package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.common.PLAYLISTMAKER_PREFERENCES
import com.example.playlistmaker.player.data.MediaPlayerWrapper
import com.example.playlistmaker.player.data.impl.MediaPlayerWrapperImpl
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.repository.SearchHistory
import com.example.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.android.ext.koin.androidContext


val dataModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(ItunesApi::class.java) }
    single { androidContext().getSharedPreferences(
        PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE
    ) }
    single { SearchHistory(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }
    single<MediaPlayerWrapper> { MediaPlayerWrapperImpl() }
}