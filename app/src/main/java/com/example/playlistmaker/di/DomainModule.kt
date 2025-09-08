package com.example.playlistmaker.di

import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.impl.GetSearchHistoryUseCaseImpl
import com.example.playlistmaker.search.domain.api.GetSearchHistoryUseCase
import com.example.playlistmaker.search.domain.impl.SaveSearchTermUseCaseImpl
import com.example.playlistmaker.search.domain.api.SaveSearchTermUseCase
import com.example.playlistmaker.search.domain.impl.ClearSearchHistoryUseCaseImpl
import com.example.playlistmaker.search.domain.api.ClearSearchHistoryUseCase
import com.example.playlistmaker.settings.domain.impl.GetSettingsInteractorImpl
import com.example.playlistmaker.settings.domain.api.GetSettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SaveSettingsInteractorImpl
import com.example.playlistmaker.settings.domain.api.SaveSettingsInteractor
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.impl.PlaylistInteractorImpl
import org.koin.dsl.module

val domainModule = module {

    factory<SearchTracksInteractor> { SearchTracksInteractorImpl(get()) }
    factory<GetSearchHistoryUseCase> { GetSearchHistoryUseCaseImpl(get()) }
    factory<SaveSearchTermUseCase> { SaveSearchTermUseCaseImpl(get()) }
    factory<ClearSearchHistoryUseCase> { ClearSearchHistoryUseCaseImpl(get()) }

    factory<GetSettingsInteractor> { GetSettingsInteractorImpl(get()) }
    factory<SaveSettingsInteractor> { SaveSettingsInteractorImpl(get()) }

    factory<PlayerInteractor> { PlayerInteractorImpl(get()) }

    factory<PlaylistInteractor> { PlaylistInteractorImpl(get()) }
}
