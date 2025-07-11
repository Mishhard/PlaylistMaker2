package com.example.playlistmaker.di

import com.example.playlistmaker.library.data.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.library.domain.db.FavoriteTracksRepository
import org.koin.dsl.module
import com.example.playlistmaker.library.data.converters.TrackDbConverter

val repositoryModule = module {
    single { TrackDbConverter() }
    factory<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(
            get(),
            get()
        )
    }
}
