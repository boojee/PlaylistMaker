package com.go.playlistmaker.audioplayer.ui.playlistbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.go.playlistmaker.R
import com.go.playlistmaker.audioplayer.ui.AudioPlayerFragment.Companion.IS_FAVORITE
import com.go.playlistmaker.createplaylist.ui.fragments.CreatePlaylistFragment
import com.go.playlistmaker.playlists.domain.models.PlaylistDomain
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        private const val TRACK_ID = "TRACK_ID"
        private const val TRACK_NAME = "TRACK_NAME"
        private const val ARTIST_NAME = "ARTIST_NAME"
        private const val TRACK_TIME = "TRACK_TIME"
        private const val ARTWORK_URL = "ARTWORK_URL"
        private const val COLLECTION_NAME = "COLLECTION_NAME"
        private const val RELEASE_DATE = "RELEASE_DATE"
        private const val PRIMARY_GENRE_NAME = "PRIMARY_GENRE_NAME"
        private const val COUNTRY = "COUNTRY"
        private const val PREVIEW_URL = "PREVIEW_URL"

        fun newInstance(
            trackId: Long,
            trackName: String,
            artistName: String,
            trackTimeMillis: String,
            artworkUrl100: String,
            collectionName: String,
            releaseDate: String,
            primaryGenreName: String,
            country: String,
            previewUrl: String,
            isFavorite: Boolean
        ): PlaylistBottomSheetFragment {
            val fragment = PlaylistBottomSheetFragment()
            val args = bundleOf(
                TRACK_ID to trackId,
                TRACK_NAME to trackName,
                ARTIST_NAME to artistName,
                TRACK_TIME to trackTimeMillis,
                ARTWORK_URL to artworkUrl100,
                COLLECTION_NAME to collectionName,
                RELEASE_DATE to releaseDate,
                PRIMARY_GENRE_NAME to primaryGenreName,
                COUNTRY to country,
                PREVIEW_URL to previewUrl,
                IS_FAVORITE to isFavorite
            )
            fragment.arguments = args
            return fragment
        }
    }

    private var trackId: Long? = null
    private var playlist: PlaylistDomain? = null
    private var trackName: String? = null
    private var artistName: String? = null
    private var trackTimeMillis: String? = null
    private var artworkUrl100: String? = null
    private var collectionName: String? = null
    private var releaseDate: String? = null
    private var primaryGenreName: String? = null
    private var country: String? = null
    private var previewUrl: String? = null
    private var isFavorite: Boolean? = null

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

        trackId = arguments?.getLong(TRACK_ID)
        trackName = arguments?.getString(TRACK_NAME)
        artistName = arguments?.getString(ARTIST_NAME)
        trackTimeMillis = arguments?.getString(TRACK_TIME)
        artworkUrl100 = arguments?.getString(ARTWORK_URL)
        collectionName = arguments?.getString(COLLECTION_NAME)
        releaseDate = arguments?.getString(RELEASE_DATE)
        primaryGenreName = arguments?.getString(PRIMARY_GENRE_NAME)
        country = arguments?.getString(COUNTRY)
        previewUrl = arguments?.getString(PREVIEW_URL)
        isFavorite = arguments?.getBoolean(IS_FAVORITE)


        val recyclerView = view.findViewById<RecyclerView>(R.id.playlistRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = PlaylistAdapter { playlist ->
            if (trackId != null) {
                playlist.playlistId?.let {
                    playlistBottomSheetViewModel.checkContainsTrackIdInPlaylist(
                        playlistId = it,
                        trackId = trackId!!
                    )
                }
                this.playlist = playlist
            }
        }
        recyclerView.adapter = adapter

        view.findViewById<View>(R.id.createPlaylistButton).setOnClickListener {
            findNavController().navigate(
                R.id.createPlaylistFragment,
                CreatePlaylistFragment.createArgs(true)
            )
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
                            insertTrack(playlist, trackId)
                        }
                    }
                    is PlaylistState.TrackAdded -> {
                        val text = requireContext().resources.getString(
                            R.string.track_has_been_added_to_playlist,
                            playlist?.playlistName
                        )
                        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                }
            }
    }

    private fun insertTrack(playlist: PlaylistDomain?, trackId: Long?) {
        if (playlist != null && trackId != null) {
            val track = TrackDomain(
                trackId = trackId,
                trackName = trackName.orEmpty(),
                artistName = artistName.orEmpty(),
                trackTimeMillis = trackTimeMillis.orEmpty(),
                artworkUrl100 = artworkUrl100.orEmpty(),
                collectionName = collectionName.orEmpty(),
                releaseDate = releaseDate.orEmpty(),
                primaryGenreName = primaryGenreName.orEmpty(),
                country = country.orEmpty(),
                previewUrl = previewUrl.orEmpty(),
                isFavorite = isFavorite ?: false
            )
            playlist.playlistId?.let { playlistBottomSheetViewModel.insertTrack(it, track) }
        }
    }

    override fun onStart() {
        super.onStart()
        playlistBottomSheetViewModel.getPlaylist()
    }
}