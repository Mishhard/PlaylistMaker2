package com.example.playlistmaker.library.data.db.dao

import androidx.room.*
import com.example.playlistmaker.library.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.library.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks WHERE trackId IN (:ids)")
    suspend fun getTracksByIds(ids: List<Int>): List<TrackEntity>

    @Query("DELETE FROM tracks WHERE trackId NOT IN (:ids)")
    suspend fun deleteTracksNotInIds(ids: List<Int>)

    @Query("DELETE FROM tracks")
    suspend fun clearAllTracks()
}
