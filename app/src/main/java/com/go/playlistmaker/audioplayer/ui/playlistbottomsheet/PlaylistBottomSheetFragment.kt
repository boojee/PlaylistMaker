package com.go.playlistmaker.audioplayer.ui.playlistbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.go.playlistmaker.R
import com.go.playlistmaker.playlists.data.db.Playlist
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistBottomSheetFragment : BottomSheetDialogFragment() {

    private var trackId: Long? = null
    private var playlist: Playlist? = null

    companion object {
        private const val ARG_TRACK_ID = "track_id"

        fun newInstance(trackId: Long): PlaylistBottomSheetFragment {
            val fragment = PlaylistBottomSheetFragment()
            val args = Bundle().apply {
                putLong(ARG_TRACK_ID, trackId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private val playlistBottomSheetViewModel: PlaylistBottomSheetViewModel by viewModel()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackId = arguments?.getLong(ARG_TRACK_ID)

        val recyclerView = view.findViewById<RecyclerView>(R.id.playlistRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = PlaylistAdapter { playlist ->
            if (trackId != null) {
                playlistBottomSheetViewModel.checkContainsTrackIdInPlaylist(
                    playlistId = playlist.playlistId,
                    trackId = trackId!!
                )
                this.playlist = playlist
            }
        }
        recyclerView.adapter = adapter

        view.findViewById<View>(R.id.createPlaylistButton).setOnClickListener {
            findNavController().navigate(R.id.createPlaylistFragment)
            dismiss()
        }

        playlistBottomSheetViewModel.getPlaylist()

        playlistBottomSheetViewModel.getPlaylistStateLiveData()
            .observe(viewLifecycleOwner) { playlistState ->
                when (playlistState) {
                    is PlaylistState.PlaylistList -> {
                        adapter.submitList(playlistState.playlistList)
                    }

                    is PlaylistState.CheckTrackId -> {
                        if (playlistState.isContains) {
                            val text = requireContext().resources.getString(
                                R.string.track_already_added_to_playlist,
                                playlist?.playlistName
                            )
                            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
                        } else {
                            if (playlist?.playlistId != null && trackId != null) {
                                val text = requireContext().resources.getString(
                                    R.string.track_has_been_added_to_playlist,
                                    playlist?.playlistName
                                )
                                Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
                                playlistBottomSheetViewModel.updatePlaylistTrackIds(
                                    playlist?.playlistId!!,
                                    trackId!!.toInt()
                                )
                                playlistBottomSheetViewModel.getPlaylist()
                            }
                        }
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        playlistBottomSheetViewModel.getPlaylist()
    }
}