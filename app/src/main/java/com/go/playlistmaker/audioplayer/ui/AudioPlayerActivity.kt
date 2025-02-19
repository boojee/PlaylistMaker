package com.go.playlistmaker.audioplayer.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.go.playlistmaker.R
import com.go.playlistmaker.databinding.ActivityAudioPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_TRACK_TIME = "00:00"
        private const val FORMAT_IMAGE_ALBUM = "512x512bb.jpg"
    }

    private val audioPlayerViewModel by viewModel<AudioPlayerViewModel>()
    private lateinit var binding: ActivityAudioPlayerBinding

    private var trackName: String? = null
    private var artistName: String? = null
    private var trackTimeMillis: String? = null
    private var artworkUrl100: String? = null
    private var collectionName: String? = null
    private var releaseDate: String? = null
    private var primaryGenreName: String? = null
    private var country: String? = null
    private var previewUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent.extras
        if (intent != null) {
            trackName = intent.getString("TRACK_NAME")
            artistName = intent.getString("ARTIST_NAME")
            trackTimeMillis = intent.getString("TRACK_TIME")
            artworkUrl100 = intent.getString("ARTWORK_URL")
            collectionName = intent.getString("COLLECTION_NAME")
            releaseDate = intent.getString("RELEASE_DATE")
            primaryGenreName = intent.getString("PRIMARY_GENRE_NAME")
            country = intent.getString("COUNTRY")
            previewUrl = intent.getString("PREVIEW_URL")
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.audioPlayer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        previewUrl?.let { audioPlayerViewModel.preparePlayer(it) }

        audioPlayerViewModel.getTrackStateLiveData().observe(this) { trackState ->
            when (trackState) {
                TrackState.Prepared -> {
                    binding.playButton.setImageDrawable(getDrawable(R.drawable.ic_play))
                    binding.playbackTime.text = DEFAULT_TRACK_TIME
                }

                TrackState.Paused -> {
                    binding.playButton.setImageDrawable(getDrawable(R.drawable.ic_play))
                }

                is TrackState.Playing -> {
                    binding.playButton.setImageDrawable(getDrawable(R.drawable.ic_pause))
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
                    ?: R.drawable.ic_track_placeholder)
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
        binding.buttonBack.setOnClickListener { finish() }
        binding.playButton.setOnClickListener { audioPlayerViewModel.playbackControl() }
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