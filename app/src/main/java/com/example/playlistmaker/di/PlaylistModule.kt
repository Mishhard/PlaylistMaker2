package com.example.playlistmaker.di

import com.example.playlistmaker.library.data.PlaylistRepositoryImpl
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.dao.PlaylistDao
import com.example.playlistmaker.library.data.db.dao.TrackDao
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import org.koin.dsl.module

val playlistModule = module {

    single<PlaylistDao> {
        get<AppDatabase>().playlistDao()
    }

    single<TrackDao> {
        get<AppDatabase>().trackDao()
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            playlistDao = get(),
            trackDao = get()
        )
    }
}
