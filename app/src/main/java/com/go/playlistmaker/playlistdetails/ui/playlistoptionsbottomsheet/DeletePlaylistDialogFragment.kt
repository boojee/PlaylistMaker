package com.go.playlistmaker.playlistdetails.ui.playlistoptionsbottomsheet

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.go.playlistmaker.R
import com.go.playlistmaker.playlistdetails.ui.playlistoptionsbottomsheet.PlaylistOptionsBottomSheetFragment.Companion.DELETE_PLAYLIST
import com.go.playlistmaker.playlistdetails.ui.playlistoptionsbottomsheet.PlaylistOptionsBottomSheetFragment.Companion.DELETE_PLAYLIST_REQUEST_KEY

class DeletePlaylistDialogFragment : DialogFragment() {

    companion object {
        private const val PLAYLIST_NAME = "PLAYLIST_NAME"

        fun newInstance(
            playlistName: String
        ): DeletePlaylistDialogFragment {
            val fragment = DeletePlaylistDialogFragment()
            val args = bundleOf(
                PLAYLIST_NAME to playlistName
            )
            fragment.arguments = args
            return fragment
        }
    }

    private var playlistName: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playlistName = arguments?.getString(PLAYLIST_NAME)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {

        val text = requireContext().resources.getString(
            R.string.is_playlist_delete,
            playlistName
        )
        return AlertDialog.Builder(requireContext(), R.style.LightDialogTheme)
            .setTitle(text)
            .setPositiveButton(R.string.dialog_yes) { _, _ ->
                setFragmentResult(
                    DELETE_PLAYLIST_REQUEST_KEY,
                    bundleOf(DELETE_PLAYLIST to true)
                )
                dismiss()
            }
            .setNegativeButton(R.string.dialog_no) { _, _ ->
                dismiss()
            }
            .create()
    }
}