package com.go.playlistmaker.playlistdetails.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.go.playlistmaker.R
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain

class TrackAdapter(
    private val onTrackClick: (TrackDomain) -> Unit = {},
    private val onTrackLongClick: (TrackDomain) -> Unit = {}
) : ListAdapter<TrackDomain, TrackAdapter.TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_list_item, parent, false)
        return TrackViewHolder(view, onTrackClick, onTrackLongClick)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TrackViewHolder(
        itemView: View,
        private val onTrackClick: (TrackDomain) -> Unit,
        private val onTrackLongClick: (TrackDomain) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val albumIcon: ImageView = itemView.findViewById(R.id.album_cover)
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val artistName: TextView = itemView.findViewById(R.id.artist_name)
        private val trackTime: TextView = itemView.findViewById(R.id.track_time)

        fun bind(track: TrackDomain) {
            itemView.apply {
                trackName.text = track.trackName
                artistName.text = track.artistName
                trackTime.text = track.trackTimeMillis

                Glide.with(itemView)
                    .load(track.artworkUrl100)
                    .placeholder(R.drawable.ic_track_placeholder)
                    .centerCrop()
                    .transform(RoundedCorners(2))
                    .into(albumIcon)

                setOnClickListener { onTrackClick(track) }
                setOnLongClickListener {
                    onTrackLongClick(track)
                    true
                }
                }
            }
        }
    }

class TrackDiffCallback : DiffUtil.ItemCallback<TrackDomain>() {
    override fun areItemsTheSame(oldItem: TrackDomain, newItem: TrackDomain): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: TrackDomain, newItem: TrackDomain): Boolean {
        return oldItem == newItem
    }
}