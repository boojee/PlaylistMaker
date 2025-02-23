package com.go.playlistmaker.medialibrary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.go.playlistmaker.R
import com.go.playlistmaker.medialibrary.ui.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlists, container, false)

        view.findViewById<Button>(R.id.button_new_playlist).setOnClickListener {
            // Обработка нажатия на кнопку (пока не реализована)
        }
        return view
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}