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
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain
import java.io.File

class PlaylistAdapter(
    private val onItemClick: (PlaylistDomain) -> Unit
) : ListAdapter<PlaylistDomain, PlaylistAdapter.PlaylistViewHolder>(PlaylistDiffCallback()) {

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
        private val onItemClick: (PlaylistDomain) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(playlist: PlaylistDomain) {
            val playlistImage = itemView.findViewById<ImageView>(R.id.playlist_image)
            itemView.findViewById<TextView>(R.id.playlist_name).text = playlist.playlistName

            val trackCountText = itemView.context.resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.playlistTracksCount,
                playlist.playlistTracksCount
            )
            itemView.findViewById<TextView>(R.id.playlist_track_count).text = trackCountText

            val imagePath = playlist.playlistUri
            if (imagePath.isNotEmpty()) {
                val cleanPath = imagePath.removePrefix("file:")
                val file = File(cleanPath)

                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    playlistImage.setImageBitmap(bitmap)
                } else {
                    playlistImage.setImageResource(R.drawable.ic_track_placeholder)
                }
            } else {
                playlistImage.setImageResource(R.drawable.ic_track_placeholder)
            }

            itemView.setOnClickListener {
                onItemClick(playlist)
            }
        }
    }
}

class PlaylistDiffCallback : DiffUtil.ItemCallback<PlaylistDomain>() {
    override fun areItemsTheSame(oldItem: PlaylistDomain, newItem: PlaylistDomain): Boolean {
        return oldItem.playlistId == newItem.playlistId
    }

    override fun areContentsTheSame(oldItem: PlaylistDomain, newItem: PlaylistDomain): Boolean {
        return oldItem == newItem
    }
}