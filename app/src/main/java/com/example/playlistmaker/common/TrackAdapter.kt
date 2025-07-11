package com.example.playlistmaker.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.player.ui.models.TrackUiModel

class TracksAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<TrackUiModel>()
    private var onItemClickListener: ((TrackUiModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(track)
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun setOnItemClickListener(listener: (TrackUiModel) -> Unit) {
        onItemClickListener = listener
    }
}
