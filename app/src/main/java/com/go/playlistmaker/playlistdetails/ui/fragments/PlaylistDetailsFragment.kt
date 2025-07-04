package com.go.playlistmaker.playlistdetails.ui.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.go.playlistmaker.R
import com.go.playlistmaker.audioplayer.ui.AudioPlayerFragment
import com.go.playlistmaker.databinding.FragmentViewPlaylistBinding
import com.go.playlistmaker.playlistdetails.data.db.Track
import com.go.playlistmaker.playlistdetails.ui.PlaylistDetailsState
import com.go.playlistmaker.playlistdetails.ui.PlaylistDetailsViewModel
import com.go.playlistmaker.playlistdetails.ui.TrackAdapter
import com.go.playlistmaker.playlistdetails.ui.models.TrackModel
import com.go.playlistmaker.playlistdetails.ui.playlistoptionsbottomsheet.PlaylistOptionsBottomSheetFragment
import com.go.playlistmaker.playlistdetails.ui.playlistviewbottomsheet.DeleteTrackDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistDetailsFragment : Fragment() {

    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"
        private const val TRACKS_IDS = "TRACKS_IDS"
        const val DELETE_TRACK_REQUEST_KEY = "DELETE_TRACK_REQUEST_KEY"
        const val DELETE_TRACK = "DELETE_TRACK"
        private const val PLAYLIST_NAME = "PLAYLIST_NAME"
        private const val PLAYLIST_DESCRIPTION = "PLAYLIST_DESCRIPTION"
        private const val PLAYLIST_TRACK_COUNT = "PLAYLIST_TRACK_COUNT"
        private const val TRACK_INFO = "TRACK_INFO"

        fun createArgs(
            playlistId: Long,
            tracksIds: List<Long>,
            playlistName: String,
            playlistDescription: String
        ): Bundle =
            bundleOf(
                PLAYLIST_ID to playlistId,
                TRACKS_IDS to tracksIds.toLongArray(),
                PLAYLIST_NAME to playlistName,
                PLAYLIST_DESCRIPTION to playlistDescription
            )
    }

    private val playlistsDetailsViewModel: PlaylistDetailsViewModel by viewModel()
    private lateinit var binding: FragmentViewPlaylistBinding

    private var playlistId: Long? = null
    private var trackId: Long? = null
    private var trackList: List<TrackModel> = emptyList()
    private var playlistNameArg: String? = null
    private var playlistDescriptionArg: String? = null
    private var playlistTrackCountArg: Int? = null
    private var trackInfoList: List<TrackModel>? = null

    private val adapter = TrackAdapter(
        onTrackClick = { track: Track ->
            findNavController().navigate(
                R.id.audioPlayerFragment,
                AudioPlayerFragment.createArgs(
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = track.trackTimeMillis,
                    artworkUrl100 = track.artworkUrl100,
                    collectionName = track.collectionName,
                    releaseDate = track.releaseDate,
                    primaryGenreName = track.primaryGenreName,
                    country = track.country,
                    previewUrl = track.previewUrl,
                    isFavorite = track.isFavorite
                )
            )
        },
        onTrackLongClick = { track ->
            openDeleteTrackDialog()
            trackId = track.trackId
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            DELETE_TRACK_REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            if (bundle.getBoolean(DELETE_TRACK)) {
                if (playlistId != null && trackId != null) {
                    playlistsDetailsViewModel.deleteTrackFromPlaylist(
                        playlistId!!,
                        trackId!!.toInt()
                    )
                }
            }
        }

        playlistNameArg = arguments?.getString(PLAYLIST_NAME)
        playlistDescriptionArg = arguments?.getString(PLAYLIST_DESCRIPTION)
        playlistTrackCountArg = arguments?.getInt(PLAYLIST_TRACK_COUNT)
        trackInfoList = arguments?.getParcelableArrayList(TRACK_INFO)

        binding.root.post {

            fun View.getHeightWithMargins(): Int {
                val params = layoutParams as? ViewGroup.MarginLayoutParams
                return height + (params?.topMargin ?: 0) + (params?.bottomMargin ?: 0)
            }

            val allElementsHeight = with(binding) {
                imageContainer.height
                    .plus(playlistName.getHeightWithMargins())
                    .plus(playlistDescription.getHeightWithMargins())
                    .plus(trackInfo.getHeightWithMargins())
                    .plus(playlistOptions.getHeightWithMargins())
                    .plus(resources.getDimensionPixelSize(R.dimen.margin_default))
            }
            val bottomSheetHeight = binding.root.height.minus(allElementsHeight)

            BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
                peekHeight = bottomSheetHeight
            }
        }

        playlistId = arguments?.getLong(PLAYLIST_ID)
        val tracksIds = arguments?.getLongArray(TRACKS_IDS)?.toList() ?: emptyList()

        binding.playlistRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.playlistRecyclerView.adapter = adapter
        playlistsDetailsViewModel.getAllTracksForPlaylist(tracksIds)

        playlistsDetailsViewModel.playlistDetailsLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistDetailsState.PlaylistDetails -> {
                    val playlist = state.playlist

                    binding.apply {
                        backButton.setOnClickListener { findNavController().navigateUp() }
                        playlistName.text = playlist.playlistName
                        playlistDescription.text = playlist.playlistDescription
                        val trackCountText = context?.resources?.getQuantityString(
                            R.plurals.tracks_count,
                            playlist.playlistTracksCount,
                            playlist.playlistTracksCount
                        )
                        playlistTrackCount.text = trackCountText
                        val trackCountMinuteText = context?.resources?.getQuantityString(
                            R.plurals.tracks_count_minute,
                            playlist.playlistTrackTiming,
                            playlist.playlistTrackTiming
                        )
                        playlistTracksTiming.text = trackCountMinuteText

                        val imagePath = playlist.playlistUri
                        if (imagePath.isNotEmpty()) {
                            val cleanPath = imagePath.removePrefix("file:")
                            val file = File(cleanPath)
                            if (file.exists()) {
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                imageContainer.setImageBitmap(bitmap)
                            } else {
                                imageContainer.setImageResource(R.drawable.ic_track_placeholder)
                            }
                        } else {
                            imageContainer.setImageResource(R.drawable.ic_track_placeholder)
                        }

                        playlistShareIcon.setOnClickListener {
                            val tracksToShare = adapter.currentList.map { track ->
                                TrackModel(
                                    trackName = track.trackName,
                                    trackAuthor = track.artistName,
                                    trackTime = track.trackTimeMillis
                                )
                            }
                            val message = createShareMessage(
                                playlistNameArg.orEmpty(),
                                playlistDescriptionArg.orEmpty(),
                                tracksToShare.size,
                                tracksToShare
                            )
                            val sendMessage = Intent(Intent.ACTION_SEND)
                            sendMessage.type = "text/plain"
                            sendMessage.putExtra(Intent.EXTRA_TEXT, message)
                            val shareIntent = Intent.createChooser(sendMessage, null)
                            startActivity(shareIntent)
                        }

                        playlistOptions.setOnClickListener {
                            val bottomSheetFragment =
                                PlaylistOptionsBottomSheetFragment.newInstance(
                                    playlistName = playlist.playlistName,
                                    playlistDescription = playlist.playlistDescription,
                                    playlistImage = playlist.playlistUri,
                                    playlistTrackCount = playlist.playlistTracksCount,
                                    playlistId = playlist.playlistId,
                                    trackInfoList = trackList
                                )
                            bottomSheetFragment.show(
                                parentFragmentManager,
                                "PlaylistOptionsBottomSheetFragmentTag"
                            )
                        }
                    }
                }

                is PlaylistDetailsState.TracksDetails -> {
                    val tracks = state.tracks
                    val localTracks: MutableList<TrackModel> = mutableListOf()

                    tracks.map { track ->
                        localTracks.add(
                            TrackModel(
                                trackName = track.trackName,
                                trackAuthor = track.artistName,
                                trackTime = track.trackTimeMillis
                            )
                        )
                    }
                    trackList = localTracks

                    binding.apply {
                        adapter.submitList(tracks)
                    }
                }
            }
        }
        playlistId?.let { playlistsDetailsViewModel.getPlaylistDetailsById(it) }
    }

    private fun openDeleteTrackDialog() {
        val bottomSheetFragment = DeleteTrackDialogFragment()
        bottomSheetFragment.show(
            parentFragmentManager,
            "openDeleteTrackDialogTag"
        )
    }

    private fun createShareMessage(
        playlistName: String,
        playlistDescription: String,
        playlistTrackCount: Int,
        trackInfoList: List<TrackModel>
    ): String {
        val message = StringBuilder()

        message.append(playlistName)
        message.append("\n").append(playlistDescription)
        message.append("\n").append("[${playlistTrackCount.toString().padStart(2, '0')}] треков")
        trackInfoList.forEachIndexed { index, track ->
            message.append("\n")
                .append("${index + 1}. ${track.trackAuthor} - ${track.trackName} (${track.trackTime})")
        }

        return message.toString()
    }
}