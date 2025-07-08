package com.go.playlistmaker.searchtrack.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.go.playlistmaker.R
import com.go.playlistmaker.searchtrack.domain.models.TrackDomain
import com.go.playlistmaker.audioplayer.ui.AudioPlayerFragment
import com.go.playlistmaker.databinding.FragmentSearchBinding
import com.go.playlistmaker.searchtrack.ui.adapters.TrackAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 2000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val trackSearchViewModel by viewModel<TrackSearchViewModel>()
    private lateinit var binding: FragmentSearchBinding

    private var editTextContent: String? = null
    private val musicList: MutableList<TrackDomain> = mutableListOf()
    private var lastSearchQuery: String? = null
    private var isLastRequestFailed: Boolean = false
    private var isClickAllowed = true
    private var trackAdapter: TrackAdapter? = null
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editTextContent = savedInstanceState?.getString("EDIT_TEXT_CONTENT")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackSearchViewModel.getSearchStateLiveData().observe(viewLifecycleOwner) { searchState ->
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

        parentFragmentManager.setFragmentResultListener(
            AudioPlayerFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            if (requestKey == AudioPlayerFragment.REQUEST_KEY) {
                val trackId = bundle.getLong(AudioPlayerFragment.TRACK_ID_KEY)
                val isFavorite = bundle.getBoolean(AudioPlayerFragment.IS_FAVORITE)

                updateTrackFavoriteState(trackId, isFavorite)
            }
        }

        initRecyclerView()

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
                if (lastSearchQuery != s.toString()) {
                    lastSearchQuery = ""
                }
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
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())
        trackAdapter = TrackAdapter { track ->
            if (clickDebounce()) {
                findNavController().navigate(
                    R.id.action_searchFragment_to_audioPlayerFragment,
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

    private fun isVisibleHistoryMusicList(foundMusicHistory: List<TrackDomain>) {
        if (binding.editTextSearch.hasFocus() && binding.editTextSearch.text.isNullOrEmpty() && foundMusicHistory.isNotEmpty()) {
            binding.searchTextViewForHistory.isVisible = true
            binding.buttonClearSearchHistory.isVisible = true
            binding.recyclerViewSearch.isVisible = true
            trackAdapter?.setItems(foundMusicHistory)
        } else {
            hideSearchHistory()
        }
    }

    private fun stateMusicList(foundMusic: List<TrackDomain>) {
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
            binding.errorIcon.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.ic_nothing_found
                )
            )
        }
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
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
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        binding.recyclerViewSearch.isVisible = false
        binding.progressBar.isVisible = false
        binding.searchPlaceholder.isVisible = true
        binding.errorTitle.text = getString(R.string.no_internet_title)
        binding.errorDescription.isVisible = true
        binding.buttonRefresh.isVisible = true
        isLastRequestFailed = true
        binding.errorIcon.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_no_internet))
        lastSearchQuery = ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("EDIT_TEXT_CONTENT", editTextContent)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            trackSearchViewModel.viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun searchDebounce() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            findMusic(binding.editTextSearch.text.toString())
        }
    }

    private fun updateTrackFavoriteState(trackId: Long, isFavorite: Boolean) {
        val updatedList = musicList.map { track ->
            if (track.trackId == trackId) {
                track.copy(isFavorite = isFavorite)
            } else {
                track
            }
        }.toMutableList()

        musicList.clear()
        musicList.addAll(updatedList)

        trackAdapter?.setItems(musicList)
        trackAdapter?.notifyDataSetChanged()
    }
}