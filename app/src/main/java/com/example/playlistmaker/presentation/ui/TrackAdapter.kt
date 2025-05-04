// presentation/ui/TracksAdapter.kt
package com.example.playlistmaker.presentation.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track

class TracksAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()
    private var onItemClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { onItemClickListener?.invoke(track) }
    }

    override fun getItemCount(): Int = tracks.size

    fun setOnItemClickListener(listener: (Track) -> Unit) {
        onItemClickListener = listener
    }
}
