package com.go.playlistmaker.audioplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.go.playlistmaker.R
import com.go.playlistmaker.databinding.FragmentAudioPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AudioPlayerFragment : Fragment() {

    companion object {
        private const val DEFAULT_TRACK_TIME = "00:00"
        private const val FORMAT_IMAGE_ALBUM = "512x512bb.jpg"
        private const val TRACK_NAME = "TRACK_NAME"
        private const val ARTIST_NAME = "ARTIST_NAME"
        private const val TRACK_TIME = "TRACK_TIME"
        private const val ARTWORK_URL = "ARTWORK_URL"
        private const val COLLECTION_NAME = "COLLECTION_NAME"
        private const val RELEASE_DATE = "RELEASE_DATE"
        private const val PRIMARY_GENRE_NAME = "PRIMARY_GENRE_NAME"
        private const val COUNTRY = "COUNTRY"
        private const val PREVIEW_URL = "PREVIEW_URL"

        fun createArgs(
            trackName: String,
            artistName: String,
            trackTimeMillis: String,
            artworkUrl100: String,
            collectionName: String,
            releaseDate: String,
            primaryGenreName: String,
            country: String,
            previewUrl: String
        ): Bundle =
            bundleOf(
                TRACK_NAME to trackName,
                ARTIST_NAME to artistName,
                TRACK_TIME to trackTimeMillis,
                ARTWORK_URL to artworkUrl100,
                COLLECTION_NAME to collectionName,
                RELEASE_DATE to releaseDate,
                PRIMARY_GENRE_NAME to primaryGenreName,
                COUNTRY to country,
                PREVIEW_URL to previewUrl,
            )
    }

    private val audioPlayerViewModel by viewModel<AudioPlayerViewModel>()
    private lateinit var binding: FragmentAudioPlayerBinding

    private var trackName: String? = null
    private var artistName: String? = null
    private var trackTimeMillis: String? = null
    private var artworkUrl100: String? = null
    private var collectionName: String? = null
    private var releaseDate: String? = null
    private var primaryGenreName: String? = null
    private var country: String? = null
    private var previewUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackName = arguments?.getString("TRACK_NAME")
        artistName = arguments?.getString("ARTIST_NAME")
        trackTimeMillis = arguments?.getString("TRACK_TIME")
        artworkUrl100 = arguments?.getString("ARTWORK_URL")
        collectionName = arguments?.getString("COLLECTION_NAME")
        releaseDate = arguments?.getString("RELEASE_DATE")
        primaryGenreName = arguments?.getString("PRIMARY_GENRE_NAME")
        country = arguments?.getString("COUNTRY")
        previewUrl = arguments?.getString("PREVIEW_URL")

        previewUrl?.let { audioPlayerViewModel.preparePlayer(it) }

        audioPlayerViewModel.getTrackStateLiveData().observe(viewLifecycleOwner) { trackState ->
            when (trackState) {
                TrackState.Prepared -> {
                    binding.playButton.setImageDrawable(
                        getDrawable(
                            requireContext(),
                            R.drawable.ic_play
                        )
                    )
                    binding.playbackTime.text = DEFAULT_TRACK_TIME
                }

                TrackState.Paused -> {
                    binding.playButton.setImageDrawable(
                        getDrawable(
                            requireContext(),
                            R.drawable.ic_play
                        )
                    )
                }

                is TrackState.Playing -> {
                    binding.playButton.setImageDrawable(
                        getDrawable(
                            requireContext(),
                            R.drawable.ic_pause
                        )
                    )
                    binding.playbackTime.text = trackState.playbackTime
                }
            }
        }

        binding.trackName.text = trackName ?: ""
        binding.artistName.text = artistName ?: ""
        binding.trackTime.text = trackTimeMillis ?: DEFAULT_TRACK_TIME
        binding.albumCover.apply {
            Glide.with(this)
                .load(
                    artworkUrl100?.replaceAfterLast('/', FORMAT_IMAGE_ALBUM)
                        ?: R.drawable.ic_track_placeholder
                )
                .placeholder(R.drawable.ic_track_placeholder)
                .centerCrop()
                .transform(RoundedCorners(10))
                .into(this)
        }
        binding.collectionName.text = collectionName ?: ""
        binding.releaseDate.text = releaseDate?.let {
            try {
                val trimmedDateTime = it.removeSuffix("Z")
                val dateTime =
                    LocalDateTime.parse(trimmedDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                dateTime.year.toString()
            } catch (e: Exception) {
                ""
            }
        } ?: ""
        binding.primaryGenreName.text = primaryGenreName ?: ""
        binding.country.text = country ?: ""
        binding.playbackTime.text = DEFAULT_TRACK_TIME
        binding.playButton.setOnClickListener { audioPlayerViewModel.playbackControl() }
        binding.buttonBack.setOnClickListener { findNavController().navigateUp() }
    }

    override fun onPause() {
        super.onPause()
        audioPlayerViewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerViewModel.release()
    }
}