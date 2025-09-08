package com.example.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.library.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val imagePath: String? = null,
    val trackIds: String = "[]",
    val trackCount: Int = 0
)

fun PlaylistEntity.toDomain(): Playlist {
    val gson = Gson()
    val type = object : TypeToken<List<Int>>() {}.type
    val ids: List<Int> = gson.fromJson(trackIds, type) ?: emptyList()

    return Playlist(
        id = id,
        name = name,
        description = description,
        imagePath = imagePath,
        trackCount = trackCount,
        trackIds = ids
    )
}
