package com.example.playlistmaker.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.dpToPx

class BottomSheetPlaylistsAdapter(
    private val onClick: (Playlist) -> Unit
) : ListAdapter<Playlist, BottomSheetPlaylistsAdapter.PlaylistViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist_bottom_sheet, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cover: ImageView = itemView.findViewById(R.id.coverImage)
        private val name: TextView = itemView.findViewById(R.id.playlistName)
        private val count: TextView = itemView.findViewById(R.id.trackCount)

        fun bind(playlist: Playlist) {
            name.text = playlist.name
            val resources = itemView.resources
            count.text = resources.getQuantityString(
                R.plurals.track_count,
                playlist.trackCount,
                playlist.trackCount
            )
            Glide.with(itemView)
                .load(playlist.imagePath ?: R.drawable.track_placeholder)
                .transform(CenterCrop(), RoundedCorners(8.dpToPx(itemView))
                )
                .into(cover)

            itemView.setOnClickListener { onClick(playlist) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist) = oldItem == newItem
    }
}
