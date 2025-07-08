package com.go.playlistmaker.favorites.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.go.playlistmaker.R
import com.go.playlistmaker.audioplayer.ui.AudioPlayerFragment
import com.go.playlistmaker.databinding.FragmentFavoritesBinding
import com.go.playlistmaker.favorites.ui.FavoritesViewModel
import com.go.playlistmaker.favorites.ui.TrackFavoriteState
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import com.go.playlistmaker.searchtrack.ui.adapters.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val favoritesViewModel by viewModel<FavoritesViewModel>()
    private lateinit var binding: FragmentFavoritesBinding

    private var trackAdapter: TrackAdapter? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel.getTrackFavoriteStateLiveData()
            .observe(viewLifecycleOwner) { trackFavoriteState ->
                when (trackFavoriteState) {
                    is TrackFavoriteState.TrackFavoriteList -> stateTrackFavoriteList(
                        trackFavoriteState.trackDomainFavoriteList
                    )
                }
            }

        initRecyclerView()

    }

    private fun stateTrackFavoriteList(foundTracksFavorite: List<TrackDomain>) {
        if (foundTracksFavorite.isEmpty()) {
            binding.errorIcon.isVisible = true
            binding.placeholderMessage.isVisible = true
            binding.trackFavoriteList.isVisible = false
        } else {
            binding.errorIcon.isVisible = false
            binding.placeholderMessage.isVisible = false
            binding.trackFavoriteList.isVisible = true
            trackAdapter?.setItems(foundTracksFavorite)
        }

    }

    private fun initRecyclerView() {
        binding.trackFavoriteList.layoutManager = LinearLayoutManager(requireContext())
        trackAdapter = TrackAdapter { track ->
            if (clickDebounce()) {
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
            }
        }
        binding.trackFavoriteList.adapter = trackAdapter
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            favoritesViewModel.viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 2000L
    }
}