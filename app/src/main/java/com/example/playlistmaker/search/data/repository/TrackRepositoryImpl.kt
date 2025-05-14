package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.dto.SearchResponse
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.domain.api.TrackRepository

class TrackRepositoryImpl(
    private val api: ItunesApi,
    private val history: SearchHistory
) : TrackRepository {

    override suspend fun searchTracks(term: String): List<Track> {
        val searchResponse: SearchResponse = api.searchTracks(term)
        val dtoList: List<TrackDto> = searchResponse.results ?: emptyList()
        return dtoList.map { dto: TrackDto ->
            Track(
                trackName = dto.trackName.orEmpty(),
                artistName = dto.artistName.orEmpty(),
                artworkUrl100 = dto.artworkUrl100.orEmpty(),
                previewUrl = dto.previewUrl.orEmpty(),
                collectionName = dto.collectionName,
                releaseDate = dto.releaseDate,
                primaryGenreName = dto.primaryGenreName,
                country = dto.country,
                trackTimeMillis = dto.trackTimeMillis ?: 0L,
                trackId = dto.trackId,
                )
        }
    }

    override fun getSearchHistory(): List<Track> = history.loadHistory()

    override fun saveSearchTerm(track: Track) = history.addTrack(track)

    override fun clearSearchHistory() = history.clearHistory()

}