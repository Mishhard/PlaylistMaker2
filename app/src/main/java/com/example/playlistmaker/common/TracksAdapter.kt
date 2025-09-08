package com.example.playlistmaker.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.models.TrackUiModel
import com.example.playlistmaker.utils.dpToPx
import android.widget.ImageView
import android.widget.TextView

class TracksAdapter(
    private val onTrackClick: ((TrackUiModel) -> Unit)? = null,
    private val onTrackLongClick: ((TrackUiModel) -> Unit)? = null,
    private val roundCorners: Boolean = true
) : RecyclerView.Adapter<TracksAdapter.TrackViewHolder>() {

    var tracks: List<TrackUiModel> = emptyList()

    inner class TrackViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        ) {

        private val coverTrack: ImageView = itemView.findViewById(R.id.ivCoverTrack)
        private val trackName: TextView = itemView.findViewById(R.id.tvTrackName)
        private val artistName: TextView = itemView.findViewById(R.id.tvArtistName)
        private val trackTime: TextView = itemView.findViewById(R.id.tvTrackTime)

        fun bind(track: TrackUiModel) {
            val glideRequest = Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)

            if (roundCorners) {
                glideRequest.transform(RoundedCorners(8.dpToPx(itemView)))
            }
            glideRequest.into(coverTrack)

            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.getFormattedTime()

            itemView.setOnClickListener {
                onTrackClick?.invoke(track)
            }
            itemView.setOnLongClickListener {
                onTrackLongClick?.invoke(track)
                true
            }
        }
    }

    fun submitList(newList: List<TrackUiModel>) {
        tracks = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size
}
