package com.go.playlistmaker.presentation.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.go.playlistmaker.Creator
import com.go.playlistmaker.R
import com.go.playlistmaker.domain.api.AudioPlayerInteractor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DEFAULT_TRACK_TIME = "00:00"
        private const val FORMAT_IMAGE_ALBUM = "512x512bb.jpg"
    }

    private var playerState = STATE_DEFAULT
    private lateinit var audioPlayerInteractor: AudioPlayerInteractor

    private var buttonBackward: ImageView? = null
    private var trackNameView: TextView? = null
    private var artistNameView: TextView? = null
    private var trackTimeView: TextView? = null
    private var albumCoverView: ImageView? = null
    private var collectionNameView: TextView? = null
    private var releaseDateView: TextView? = null
    private var primaryGenreNameView: TextView? = null
    private var countryView: TextView? = null
    private var buttonPlayView: ImageButton? = null
    private var playbackTimeView: TextView? = null

    private var trackName: String? = null
    private var artistName: String? = null
    private var trackTimeMillis: String? = null
    private var artworkUrl100: String? = null
    private var collectionName: String? = null
    private var releaseDate: String? = null
    private var primaryGenreName: String? = null
    private var country: String? = null
    private var previewUrl: String? = null
    private var playbackTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audio_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        audioPlayerInteractor = Creator.provideAudioPlayerInteractor()

        previewUrl?.let {
            audioPlayerInteractor.preparePlayer(it) {
                runOnUiThread {
                    playerState = STATE_PREPARED
                    playbackTimeView?.text = DEFAULT_TRACK_TIME
                    buttonPlayView?.setImageDrawable(getDrawable(R.drawable.ic_play))
                }
            }
        }

        trackNameView = findViewById<TextView?>(R.id.track_name).apply {
            text = trackName
        }
        artistNameView = findViewById<TextView?>(R.id.artist_name).apply {
            text = artistName
        }
        trackTimeView = findViewById<TextView?>(R.id.track_time).apply {
            text = trackTimeMillis
        }
        albumCoverView = findViewById<ImageView?>(R.id.album_cover).apply {
            Glide.with(this)
                .load(artworkUrl100?.replaceAfterLast('/', FORMAT_IMAGE_ALBUM))
                .placeholder(R.drawable.ic_track_placeholder)
                .centerCrop()
                .transform(RoundedCorners(10))
                .into(this)
        }
        collectionNameView = findViewById<TextView?>(R.id.collection_name).apply {
            text = collectionName
        }
        releaseDateView = findViewById<TextView?>(R.id.release_date).apply {
            val trimmedDateTime = releaseDate?.removeSuffix("Z")
            val dateTime =
                LocalDateTime.parse(trimmedDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val year = dateTime.year
            text = year.toString()
        }
        primaryGenreNameView = findViewById<TextView?>(R.id.primary_genre_name).apply {
            text = primaryGenreName
        }
        countryView = findViewById<TextView?>(R.id.country).apply {
            text = country
        }
        playbackTimeView = findViewById<TextView?>(R.id.playback_time).apply {
            text = playbackTime ?: DEFAULT_TRACK_TIME
        }
        buttonBackward = findViewById(R.id.button_back)
        buttonBackward?.setOnClickListener {
            finish()
        }

        buttonPlayView = findViewById(R.id.play_button)

        buttonPlayView?.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayerInteractor.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerInteractor.release()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                playerState = STATE_PAUSED
                audioPlayerInteractor.pausePlayer()
                buttonPlayView?.setImageDrawable(getDrawable(R.drawable.ic_play))
            }

            STATE_PREPARED, STATE_PAUSED -> {
                playerState = STATE_PLAYING
                audioPlayerInteractor.startPlayer(object :
                    AudioPlayerInteractor.TimeUpdateCallback {
                    override fun updatePlaybackTime(time: String) {
                        runOnUiThread {
                            playbackTimeView?.text = time
                        }
                    }
                })
                buttonPlayView?.setImageDrawable(getDrawable(R.drawable.ic_pause))
            }
        }
    }
}