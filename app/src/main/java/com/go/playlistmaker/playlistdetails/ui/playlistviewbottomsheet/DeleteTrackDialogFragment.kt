package com.go.playlistmaker.playlistdetails.ui.playlistviewbottomsheet

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.go.playlistmaker.R
import com.go.playlistmaker.playlistdetails.ui.fragments.PlaylistDetailsFragment.Companion.DELETE_TRACK
import com.go.playlistmaker.playlistdetails.ui.fragments.PlaylistDetailsFragment.Companion.DELETE_TRACK_REQUEST_KEY

class DeleteTrackDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext(), R.style.LightDialogTheme)
            .setTitle(R.string.is_track_delete)
            .setPositiveButton(R.string.dialog_yes) { _, _ ->
                setFragmentResult(
                    DELETE_TRACK_REQUEST_KEY,
                    bundleOf(DELETE_TRACK to true)
                )
                dismiss()
            }
            .setNegativeButton(R.string.dialog_no) { _, _ ->
                dismiss()
            }
            .create()
    }
}