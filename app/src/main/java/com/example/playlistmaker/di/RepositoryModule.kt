package com.example.playlistmaker.di

import com.example.playlistmaker.library.data.PlaylistRepositoryImpl
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.data.converters.TrackDbConverter
import com.example.playlistmaker.library.data.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.library.domain.db.FavoriteTracksRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { TrackDbConverter() }

    factory<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(
            get(),
            get()
        )
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(get())
    }
}
