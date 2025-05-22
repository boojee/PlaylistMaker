package com.go.playlistmaker.playlists.ui

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.go.playlistmaker.R
import com.go.playlistmaker.playlists.data.db.Playlist
import java.io.File

class PlaylistAdapter(
    private val onItemClick: (Playlist) -> Unit
) : ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlaylistViewHolder(
        itemView: View,
        private val onItemClick: (Playlist) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(playlist: Playlist) {
            itemView.findViewById<TextView>(R.id.playlist_name).text = playlist.playlistName
            itemView.findViewById<TextView>(R.id.playlist_description).text =
                playlist.playlistDescription

            val imagePath = playlist.playlistUri
            if (imagePath.isNotEmpty()) {
                val cleanPath = imagePath.removePrefix("file:")
                val file = File(cleanPath)

                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    itemView.findViewById<ImageView>(R.id.playlist_image).setImageBitmap(bitmap)
                } else {
                    itemView.findViewById<ImageView>(R.id.playlist_image)
                        .setImageResource(R.drawable.ic_track_placeholder)
                }
            } else {
                itemView.findViewById<ImageView>(R.id.playlist_image)
                    .setImageResource(R.drawable.ic_track_placeholder)
            }

            itemView.setOnClickListener {
                onItemClick(playlist)
            }
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