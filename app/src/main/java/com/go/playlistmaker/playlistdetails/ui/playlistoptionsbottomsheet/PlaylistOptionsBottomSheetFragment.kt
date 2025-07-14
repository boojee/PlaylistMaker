package com.go.playlistmaker.playlistdetails.ui.playlistoptionsbottomsheet

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.go.playlistmaker.R
import com.go.playlistmaker.createplaylist.ui.fragments.CreatePlaylistFragment
import com.go.playlistmaker.databinding.FragmentPlaylistShareBottomSheetBinding
import com.go.playlistmaker.playlistdetails.ui.models.TrackModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        private const val PLAYLIST_NAME = "PLAYLIST_NAME"
        private const val PLAYLIST_DESCRIPTION = "PLAYLIST_DESCRIPTION"
        private const val PLAYLIST_TRACK_COUNT = "PLAYLIST_TRACK_COUNT"
        private const val PLAYLIST_IMAGE = "PLAYLIST_IMAGE"
        private const val PLAYLIST_ID = "PLAYLIST_ID"
        const val DELETE_PLAYLIST_REQUEST_KEY = "DELETE_PLAYLIST_REQUEST_KEY"
        const val DELETE_PLAYLIST = "DELETE_PLAYLIST"
        private const val TRACK_INFO = "TRACK_INFO"

        fun newInstance(
            playlistName: String,
            playlistDescription: String,
            playlistImage: String,
            playlistTrackCount: Int,
            playlistId: Long,
            trackInfoList: List<TrackModel>
        ): PlaylistOptionsBottomSheetFragment {
            val fragment = PlaylistOptionsBottomSheetFragment()
            val args = bundleOf(
                PLAYLIST_NAME to playlistName,
                PLAYLIST_DESCRIPTION to playlistDescription,
                PLAYLIST_TRACK_COUNT to playlistTrackCount,
                PLAYLIST_IMAGE to playlistImage,
                PLAYLIST_ID to playlistId,
                TRACK_INFO to trackInfoList
            )
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentPlaylistShareBottomSheetBinding
    private val playlistOptionsBottomSheetViewModel: PlaylistOptionsBottomSheetViewModel by viewModel()

    private var playlistNameArg: String? = null
    private var playlistDescriptionArg: String? = null
    private var playlistTrackCountArg: Int? = null
    private var playlistImageArg: String? = null
    private var playlistId: Long? = null
    private var trackInfoList: List<TrackModel>? = null

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistShareBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            DELETE_PLAYLIST_REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            if (bundle.getBoolean(DELETE_PLAYLIST)) {
                if (playlistId != null) {
                    playlistOptionsBottomSheetViewModel.deletePlaylistAndUnusedTracks(
                        playlistId!!
                    )
                    dismiss()
                    findNavController().popBackStack()
                }
            }
        }

        playlistNameArg = arguments?.getString(PLAYLIST_NAME)
        playlistDescriptionArg = arguments?.getString(PLAYLIST_DESCRIPTION)
        playlistTrackCountArg = arguments?.getInt(PLAYLIST_TRACK_COUNT)
        playlistImageArg = arguments?.getString(PLAYLIST_IMAGE)
        playlistId = arguments?.getLong(PLAYLIST_ID)
        trackInfoList = arguments?.getParcelableArrayList(TRACK_INFO)

        binding.apply {
            if (!playlistImageArg.isNullOrEmpty()) {
                val cleanPath = playlistImageArg?.removePrefix("file:")
                val file = File(cleanPath.orEmpty())

                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    playlistImage.setImageBitmap(bitmap)
                } else {
                    playlistImage.setImageResource(R.drawable.ic_track_placeholder)
                }
            } else {
                playlistImage.setImageResource(R.drawable.ic_track_placeholder)
            }
            playlistName.text = playlistNameArg

            val trackCountText = context?.resources?.getQuantityString(
                R.plurals.tracks_count,
                playlistTrackCountArg ?: 0,
                playlistTrackCountArg
            )
            playlistTrackCount.text = trackCountText
            buttonEditPlaylist.setOnClickListener {
                findNavController().navigate(
                    R.id.createPlaylistFragment,
                    CreatePlaylistFragment.createArgs(false, playlistId = playlistId)
                )
                dismiss()
            }
            buttonDeletePlaylist.setOnClickListener {
                openDeletePlaylistDialog()
            }
            buttonShare.setOnClickListener {
                if (playlistTrackCountArg == 0 || trackInfoList.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(), R.string.track_share_is_empty,
                        Toast.LENGTH_SHORT
                    ).show()
                    dismiss()
                } else {
                    val message = createShareMessage(
                        playlistNameArg.orEmpty(),
                        playlistDescriptionArg.orEmpty(),
                        playlistTrackCountArg ?: 0,
                        trackInfoList.orEmpty()
                    )
                    val sendMessage = Intent(Intent.ACTION_SEND)
                    val shareIntent = Intent.createChooser(sendMessage, null)
                    sendMessage.type = "text/plain"
                    sendMessage.putExtra(Intent.EXTRA_TEXT, message)
                    startActivity(shareIntent)
                }
            }
        }
    }

    private fun openDeletePlaylistDialog() {
        val bottomSheetFragment =
            DeletePlaylistDialogFragment.newInstance(playlistName = playlistNameArg.orEmpty())
        bottomSheetFragment.show(
            parentFragmentManager,
            "openDeletePlaylistDialogTag"
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