package com.go.playlistmaker.playlistdetails.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackModel(
    val trackName: String,
    val trackAuthor: String,
    val trackTime: String
) : Parcelable