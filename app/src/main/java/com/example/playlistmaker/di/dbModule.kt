package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.library.data.db.AppDatabase
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext


val dbModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "playlist_maker_database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().trackDao() }
    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().favoriteTrackDao() }
}
