package com.example.playlistmaker.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.library.data.dto.PlaylistDto

class PlaylistAdapter : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    private var items: List<PlaylistDto> = emptyList()

    fun submitList(newItems: List<PlaylistDto>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cover = itemView.findViewById<ImageView>(R.id.coverImage)
        private val name = itemView.findViewById<TextView>(R.id.playlistName)
        private val count = itemView.findViewById<TextView>(R.id.trackCount)

        fun bind(playlist: PlaylistDto) {
            name.text = playlist.name
            count.text = "${playlist.trackCount} треков"

            if (playlist.imagePath != null) {
                Glide.with(itemView.context)
                    .load(playlist.imagePath)
                    .transform(CenterCrop(), RoundedCorners(8))
                    .placeholder(R.drawable.track_placeholder)
                    .into(cover)
            } else {
                cover.setImageResource(R.drawable.track_placeholder)
            }
        }
    }
}
