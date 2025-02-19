package com.go.playlistmaker.searchtrack.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.go.playlistmaker.R
import com.go.playlistmaker.databinding.ActivitySearchBinding
import com.go.playlistmaker.searchtrack.domain.models.Track
import com.go.playlistmaker.audioplayer.ui.AudioPlayerActivity
import com.go.playlistmaker.searchtrack.ui.adapters.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 2000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val trackSearchViewModel by viewModel<TrackSearchViewModel>()
    private lateinit var binding: ActivitySearchBinding

    private var editTextContent: String? = null
    private val musicList: MutableList<Track> = mutableListOf()
    private var lastSearchQuery: String? = null
    private var isLastRequestFailed: Boolean = false
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var trackAdapter: TrackAdapter? = null

    private val searchRunnable = Runnable {
        findMusic(binding.editTextSearch.text.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackSearchViewModel.getSearchStateLiveData().observe(this) { searchState ->
            when (searchState) {
                is SearchState.SearchLoading -> {
                    binding.progressBar.isVisible = searchState.isLoading
                }
                is SearchState.MusicList -> stateMusicList(searchState.musicList)
                is SearchState.HistoryMusicList -> {
                    isVisibleHistoryMusicList(searchState.musicList)
                }
                is SearchState.SearchError -> showErrorMessage(searchState.message)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.search) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initRecyclerView()

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonClear.isVisible = !binding.editTextSearch.text.isNullOrEmpty()

        binding.buttonClear.setOnClickListener {
            binding.editTextSearch.setText("")
            binding.editTextSearch.clearFocus()
            lastSearchQuery = ""
            hideKeyboard()
            hideSearchResults()
        }

        val simpleEditTextSearch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextContent = s?.toString()
                lastSearchQuery = ""
                val query = binding.editTextSearch.text?.toString()?.trim()
                if (!query.isNullOrEmpty()) {
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                binding.buttonClear.isVisible = !s.isNullOrEmpty()

                if (s.isNullOrEmpty()) {
                    hideSearchHistory()
                    hideSearchResults()
                    binding.searchPlaceholder.isVisible = false
                } else {
                    hideSearchHistory()
                    binding.recyclerViewSearch.isVisible = false
                    binding.searchPlaceholder.isVisible = false
                }
            }
        }

        binding.editTextSearch.addTextChangedListener(simpleEditTextSearch)

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findMusic(binding.editTextSearch.text.toString())
                true
            }
            false
        }

        binding.editTextSearch.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editTextSearch.text.isNullOrEmpty()) {
                trackSearchViewModel.findMusicHistory()
                binding.searchPlaceholder.isVisible = false
            } else {
                hideSearchHistory()
            }
        }

        binding.buttonRefresh.setOnClickListener {
            if (isLastRequestFailed && lastSearchQuery != null) {
                findMusic(binding.editTextSearch.text.toString())
            }
        }

        binding.buttonClearSearchHistory.setOnClickListener {
            trackSearchViewModel.clearHistory()
            hideSearchHistory()
        }

        if (savedInstanceState != null) {
            editTextContent = savedInstanceState.getString("EDIT_TEXT_CONTENT")
            binding.editTextSearch.setText(editTextContent)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter { track ->
            if (clickDebounce()) {
                val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java)
                val bundle = Bundle().apply {
                    putString("TRACK_NAME", track.trackName)
                    putString("ARTIST_NAME", track.artistName)
                    putString("TRACK_TIME", track.trackTimeMillis)
                    putString("ARTWORK_URL", track.artworkUrl100)
                    putString("COLLECTION_NAME", track.collectionName)
                    putString("RELEASE_DATE", track.releaseDate)
                    putString("PRIMARY_GENRE_NAME", track.primaryGenreName)
                    putString("COUNTRY", track.country)
                    putString("PREVIEW_URL", track.previewUrl)
                }

                audioPlayerIntent.putExtras(bundle)
                startActivity(audioPlayerIntent)

                trackSearchViewModel.addMusicHistory(track)
                if (binding.searchTextViewForHistory.isVisible) {
                    trackSearchViewModel.findMusicHistory()
                }
            }
        }
        binding.recyclerViewSearch.adapter = trackAdapter
    }

    private fun findMusic(text: String) {
        if (binding.progressBar.isVisible || lastSearchQuery == text) return

        lastSearchQuery = text

        musicList.clear()
        trackSearchViewModel.findMusic(lastSearchQuery.orEmpty())
    }

    private fun isVisibleHistoryMusicList(foundMusicHistory: List<Track>) {
        if (binding.editTextSearch.hasFocus() && binding.editTextSearch.text.isNullOrEmpty() && foundMusicHistory.isNotEmpty()) {
            binding.searchTextViewForHistory.isVisible = true
            binding.buttonClearSearchHistory.isVisible = true
            binding.recyclerViewSearch.isVisible = true
            trackAdapter?.setItems(foundMusicHistory)
        } else {
            hideSearchHistory()
        }
    }

    private fun stateMusicList(foundMusic: List<Track>) {
        binding.progressBar.isVisible = false
        if (foundMusic.isNotEmpty()) {
            musicList.addAll(foundMusic)
            trackAdapter?.setItems(musicList)
            trackAdapter?.notifyDataSetChanged()
            binding.recyclerViewSearch.isVisible = true
            binding.searchPlaceholder.isVisible = false
            isLastRequestFailed = false
        } else {
            binding.recyclerViewSearch.isVisible = false
            binding.searchPlaceholder.isVisible = true
            binding.errorTitle.text = getString(R.string.nothing_found)
            binding.errorDescription.isVisible = false
            binding.buttonRefresh.isVisible = false
            binding.errorIcon.setImageDrawable(getDrawable(R.drawable.ic_nothing_found))
        }
    }

    private fun hideKeyboard() {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            binding.editTextSearch.windowToken, 0
        )
    }

    private fun hideSearchHistory() {
        binding.searchTextViewForHistory.isVisible = false
        binding.buttonClearSearchHistory.isVisible = false
        binding.recyclerViewSearch.isVisible = false
    }

    private fun hideSearchResults() {
        binding.recyclerViewSearch.isVisible = false
        binding.searchPlaceholder.isVisible = false
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
        binding.recyclerViewSearch.isVisible = false
        binding.progressBar.isVisible = false
        binding.searchPlaceholder.isVisible = true
        binding.errorTitle.text = getString(R.string.no_internet_title)
        binding.errorDescription.isVisible = true
        binding.buttonRefresh.isVisible = true
        isLastRequestFailed = true
        binding.errorIcon.setImageDrawable(getDrawable(R.drawable.ic_no_internet))
        lastSearchQuery = ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("EDIT_TEXT_CONTENT", editTextContent)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextContent = savedInstanceState.getString("EDIT_TEXT_CONTENT")
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}