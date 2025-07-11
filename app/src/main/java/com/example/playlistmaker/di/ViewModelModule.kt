package com.example.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import com.example.playlistmaker.library.ui.PlaylistsViewModel
import com.example.playlistmaker.player.domain.db.FavoriteTracksViewModel


val viewModelModule = module {
    viewModel { SearchViewModel(
        get(),
        get(),
        get(),
        get()
    ) }
    viewModel { PlayerViewModel(get(), get()) }
    viewModel { SettingsViewModel(
        get(),
        get()
    ) }

    viewModel { FavoriteTracksViewModel(get()) }
    viewModel { PlaylistsViewModel() }
}