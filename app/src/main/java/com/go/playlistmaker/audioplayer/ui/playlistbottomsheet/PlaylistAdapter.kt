package com.go.playlistmaker.audioplayer.ui.playlistbottomsheet

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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_bottom_sheet_item, parent, false)
        return PlaylistViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlaylistViewHolder(
        itemView: View,
        private val onItemClick: (PlaylistDomain) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val playlistName = itemView.findViewById<TextView>(R.id.playlist_name)
        private val playlistTrackCount = itemView.findViewById<TextView>(R.id.playlist_track_count)
        private val playlistImage = itemView.findViewById<ImageView>(R.id.playlist_image)

        fun bind(playlist: PlaylistDomain) {
            playlistName.text = playlist.playlistName
            playlistTrackCount.text = playlist.playlistTracksCount.toString()

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

            itemView.setOnClickListener { onItemClick(playlist) }
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