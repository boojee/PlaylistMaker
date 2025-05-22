package com.go.playlistmaker.audioplayer.ui.playlistbottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.go.playlistmaker.R
import com.go.playlistmaker.playlists.data.db.Playlist

class PlaylistAdapter(
    private val onItemClick: (Playlist) -> Unit
) : ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_bottom_sheet_item, parent, false)
        return PlaylistViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlaylistViewHolder(
        itemView: View,
        private val onItemClick: (Playlist) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val playlistName = itemView.findViewById<TextView>(R.id.playlist_name)
        private val playlistTrackCount = itemView.findViewById<TextView>(R.id.playlist_track_count)

        fun bind(playlist: Playlist) {
            playlistName.text = playlist.playlistName
            playlistTrackCount.text = playlist.playlistTracksCount.toString()
            itemView.setOnClickListener { onItemClick(playlist) }
        }
    }
}

class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.playlistId == newItem.playlistId
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem == newItem
    }
}