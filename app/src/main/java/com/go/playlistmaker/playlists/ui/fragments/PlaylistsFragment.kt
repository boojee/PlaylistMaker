package com.go.playlistmaker.playlists.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.go.playlistmaker.R
import com.go.playlistmaker.databinding.FragmentPlaylistsBinding
import com.go.playlistmaker.playlists.ui.PlaylistAdapter
import com.go.playlistmaker.playlists.ui.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistsBinding

    private val adapter = PlaylistAdapter { playlist ->
        Toast.makeText(requireContext(), "Clicked: ${playlist.playlistName}", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)


        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.createPlaylistFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerPlaylist.adapter = adapter
        binding.recyclerPlaylist.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistsViewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            adapter.submitList(playlist)

            if (playlist.isNullOrEmpty()) {
                binding.errorIcon.visibility = View.VISIBLE
                binding.placeholderMessage.visibility = View.VISIBLE
                binding.recyclerPlaylist.visibility = View.GONE
            } else {
                binding.errorIcon.visibility = View.GONE
                binding.placeholderMessage.visibility = View.GONE
                binding.recyclerPlaylist.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}