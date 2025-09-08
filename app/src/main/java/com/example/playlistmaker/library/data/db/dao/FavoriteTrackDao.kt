package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entity.FavoriteTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: FavoriteTrackEntity)

    @Query("DELETE FROM favorite_tracks WHERE trackId = :id")
    suspend fun delete(id: Int): Int

    @Query("SELECT * FROM favorite_tracks ORDER BY trackId DESC")
    fun getAll(): Flow<List<FavoriteTrackEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tracks WHERE trackId = :id)")
    suspend fun isTrackFavorite(id: Int): Boolean
}
