package com.example.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel


val viewModelModule = module {
    viewModel { SearchViewModel(
        get(),
        get(),
        get(),
        get()
    ) }
    viewModel { PlayerViewModel(get()) }
    viewModel { SettingsViewModel(
        get(),
        get()
    ) }
}