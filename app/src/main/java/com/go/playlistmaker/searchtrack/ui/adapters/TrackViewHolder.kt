package com.go.playlistmaker.searchtrack.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.go.playlistmaker.R
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val albumIcon: ImageView = itemView.findViewById(R.id.album_cover)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    fun bind(trackDomain: TrackDomain) {
        trackName.text = trackDomain.trackName
        artistName.text = trackDomain.artistName
        trackTime.text = trackDomain.trackTimeMillis

        Glide.with(itemView)
            .load(trackDomain.artworkUrl100)
            .placeholder(R.drawable.ic_track_placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(albumIcon)

        artistName.requestLayout()
    }
}
