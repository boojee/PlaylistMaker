package com.go.playlistmaker.createplaylist.ui.cancelcreateplaylist

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.go.playlistmaker.R
import com.go.playlistmaker.createplaylist.ui.fragments.CreatePlaylistFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CancelCreatingPlaylistDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.is_finish_create_playlist)
            .setPositiveButton(R.string.finish_create_playlist) { _, _ ->
                setFragmentResult(
                    CreatePlaylistFragment.CANCEL_PLAYLIST_REQUEST_KEY,
                    bundleOf("finish_creation" to true)
                )
                dismiss()
            }
            .setNegativeButton(R.string.cancel_close_playlist) { _, _ ->
                dismiss()
            }
            .create()
    }
}