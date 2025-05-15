package com.example.playlistmaker.di

import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.search.domain.api.ClearSearchHistoryUseCase
import com.example.playlistmaker.search.domain.api.GetSearchHistoryUseCase
import com.example.playlistmaker.search.domain.api.SaveSearchTermUseCase
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.impl.ClearSearchHistoryUseCaseImpl
import com.example.playlistmaker.search.domain.impl.GetSearchHistoryUseCaseImpl
import com.example.playlistmaker.search.domain.impl.SaveSearchTermUseCaseImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.settings.domain.api.GetSettingsInteractor
import com.example.playlistmaker.settings.domain.api.SaveSettingsInteractor
import org.koin.dsl.module
import com.example.playlistmaker.settings.domain.impl.GetSettingsInteractorImpl
import com.example.playlistmaker.settings.domain.impl.SaveSettingsInteractorImpl


val domainModule = module {
    single<SearchTracksInteractor> { SearchTracksInteractorImpl(get()) }
    single<GetSearchHistoryUseCase> { GetSearchHistoryUseCaseImpl(get()) }
    single<SaveSearchTermUseCase>  { SaveSearchTermUseCaseImpl(get()) }
    single<ClearSearchHistoryUseCase> { ClearSearchHistoryUseCaseImpl(get()) }

    single<GetSettingsInteractor> { GetSettingsInteractorImpl(get()) }
    single<SaveSettingsInteractor> { SaveSettingsInteractorImpl(get()) }

    single<PlayerInteractor> { PlayerInteractorImpl(get()) }
}