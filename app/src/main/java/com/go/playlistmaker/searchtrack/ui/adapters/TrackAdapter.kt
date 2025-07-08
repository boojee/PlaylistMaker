package com.go.playlistmaker.searchtrack.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.playlistmaker.R
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain

class TrackAdapter(private val onTrackClick: (TrackDomain) -> Unit) :
    RecyclerView.Adapter<TrackViewHolder>() {

    private var trackDomains: List<TrackDomain> = mutableListOf()

    fun setItems(trackDomains: List<TrackDomain>) {
        this.trackDomains = trackDomains
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.music_list_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackDomains[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            onTrackClick(track)
        }
    }

    override fun getItemCount(): Int {
        return trackDomains.size
    }
}