package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.data.db.dao.PlaylistDao
import com.example.playlistmaker.library.data.db.dao.TrackDao
import com.example.playlistmaker.library.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity

@Database(
    version = 4,
    entities = [
        FavoriteTrackEntity::class,
        PlaylistEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}
