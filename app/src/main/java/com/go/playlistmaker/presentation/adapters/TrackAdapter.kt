package com.go.playlistmaker.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.playlistmaker.R
import com.go.playlistmaker.domain.models.Track

class TrackAdapter(private val onTrackClick: (Track) -> Unit) :
    RecyclerView.Adapter<TrackViewHolder>() {

    private var tracks: List<Track> = mutableListOf()

    fun setItems(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.music_list_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            onTrackClick(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}