package com.go.playlistmaker.createplaylist.ui.cancelcreateplaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.go.playlistmaker.R
import com.go.playlistmaker.createplaylist.ui.fragments.CreatePlaylistFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CancelCreatingPlaylistBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_cancel_creating_playlist_bottom_sheet,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.finish_create_playlist).setOnClickListener {
            setFragmentResult(
                CreatePlaylistFragment.CANCEL_PLAYLIST_REQUEST_KEY,
                bundleOf("finish_creation" to true)
            )
            dismiss()
        }
        view.findViewById<View>(R.id.cancel_close_playlist).setOnClickListener {
            dismiss()
        }
    }
}