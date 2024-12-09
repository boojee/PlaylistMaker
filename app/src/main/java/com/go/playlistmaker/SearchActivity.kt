package com.go.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val SEARCH_HISTORY_KEY = "search_history"
private const val CLICK_DEBOUNCE_DELAY = 2000L
private const val SEARCH_DEBOUNCE_DELAY = 2000L

class SearchActivity : AppCompatActivity() {

    private var editTextSearch: EditText? = null
    private var buttonClear: ImageView? = null
    private var editTextContent: String? = null
    private var buttonBackward: ImageView? = null
    private var recyclerView: RecyclerView? = null
    private val itunesRetrofit: ItunesRetrofit = ItunesRetrofit()
    private val itunesService: ItunesApi = itunesRetrofit.provideRetrofit()
    private val musicList: MutableList<Track> = mutableListOf()
    private var trackAdapter: TrackAdapter? = null
    private var errorTitle: TextView? = null
    private var errorDescription: TextView? = null
    private var errorIcon: ImageView? = null
    private var searchPlaceholder: LinearLayout? = null
    private var buttonRefresh: Button? = null
    private var lastSearchQuery: String? = null
    private var isLastRequestFailed: Boolean = false
    private var searchTextViewForHistory: TextView? = null
    private var buttonClearSearchHistory: Button? = null
    private var searchHistory: SearchHistory? = null
    private var progressBar: ProgressBar? = null

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable {
        findMusic(editTextSearch?.text.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextSearch = findViewById(R.id.edit_text_search)
        buttonClear = findViewById(R.id.button_clear)
        buttonBackward = findViewById(R.id.button_back)
        recyclerView = findViewById(R.id.recycler_view_search)
        errorTitle = findViewById(R.id.error_title)
        errorDescription = findViewById(R.id.error_description)
        errorIcon = findViewById(R.id.error_icon)
        searchPlaceholder = findViewById(R.id.search_placeholder)
        buttonRefresh = findViewById(R.id.button_refresh)
        searchTextViewForHistory = findViewById(R.id.search_text_view_for_history)
        buttonClearSearchHistory = findViewById(R.id.button_clear_search_history)
        progressBar = findViewById(R.id.progress_bar)

        initRecyclerView()

        val sharedPreferences = getSharedPreferences(SEARCH_HISTORY_KEY, Context.MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        buttonBackward?.setOnClickListener {
            finish()
        }

        buttonClear?.isVisible = !editTextSearch?.text.isNullOrEmpty()

        buttonClear?.setOnClickListener {
            editTextSearch?.setText("")
            editTextSearch?.clearFocus()

            hideKeyboard()
            hideSearchResults()
            displaySearchHistory()
        }

        val simpleEditTextSearch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextContent = s?.toString()
                val query = editTextSearch?.text?.toString()?.trim()
                if (!query.isNullOrEmpty()) {
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                buttonClear?.isVisible = !s.isNullOrEmpty()

                if (s.isNullOrEmpty()) {
                    displaySearchHistory()
                } else {
                    hideSearchHistory()
                    recyclerView?.isVisible = false
                    searchPlaceholder?.isVisible = false
                }
            }
        }

        editTextSearch?.addTextChangedListener(simpleEditTextSearch)

        editTextSearch?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.i("findMusicTest", "editTextSearch")
                findMusic(editTextSearch?.text.toString())
                true
            }
            false
        }

        editTextSearch?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editTextSearch?.text.isNullOrEmpty()) {
                displaySearchHistory()
            } else {
                hideSearchHistory()
            }
        }

        buttonRefresh?.setOnClickListener {
            if (isLastRequestFailed && lastSearchQuery != null) {
                Log.i("findMusicTest", "buttonRefresh")
                findMusic(lastSearchQuery!!)
            }
        }

        buttonClearSearchHistory?.setOnClickListener {
            searchHistory?.clearHistory()
            displaySearchHistory()
        }

        if (savedInstanceState != null) {
            editTextContent = savedInstanceState.getString("EDIT_TEXT_CONTENT")
            editTextSearch?.setText(editTextContent)
        }

        displaySearchHistory()

    }

    private fun initRecyclerView() {
        recyclerView?.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter { track ->
            if (clickDebounce()) {
                val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java)
                val bundle = Bundle().apply {
                    putString("TRACK_NAME", track.trackName)
                    putString("ARTIST_NAME", track.artistName)
                    putLong("TRACK_TIME", track.trackTimeMillis)
                    putString("ARTWORK_URL", track.artworkUrl100)
                    putString("COLLECTION_NAME", track.collectionName)
                    putString("RELEASE_DATE", track.releaseDate)
                    putString("PRIMARY_GENRE_NAME", track.primaryGenreName)
                    putString("COUNTRY", track.country)
                    putString("PREVIEW_URL", track.previewUrl)
                }

                audioPlayerIntent.putExtras(bundle)
                startActivity(audioPlayerIntent)

                searchHistory?.addTrack(track)
                if (searchTextViewForHistory?.isVisible == true) {
                    trackAdapter?.setItems(searchHistory?.getHistory() ?: emptyList())
                    trackAdapter?.notifyDataSetChanged()
                }
            }
        }
        recyclerView?.adapter = trackAdapter
    }

    private fun findMusic(text: String) {
        if (progressBar?.isVisible == true || lastSearchQuery == text) return

        lastSearchQuery = text
        progressBar?.isVisible = true

        itunesService.findMusic(text).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>, response: Response<TrackResponse>
            ) {
                progressBar?.isVisible = false

                if (response.code() == 200) {
                    musicList.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        musicList.addAll(response.body()?.results ?: listOf())
                        trackAdapter?.setItems(musicList)
                        trackAdapter?.notifyDataSetChanged()
                        recyclerView?.isVisible = true
                        searchPlaceholder?.isVisible = false
                        isLastRequestFailed = false
                    } else {
                        recyclerView?.isVisible = false
                        searchPlaceholder?.isVisible = true
                        errorTitle?.text = getString(R.string.nothing_found)
                        errorDescription?.isVisible = false
                        buttonRefresh?.isVisible = false
                        errorIcon?.setImageDrawable(getDrawable(R.drawable.ic_nothing_found))
                    }
                } else {
                    progressBar?.isVisible = false
                    Toast.makeText(
                        applicationContext, getString(R.string.something_wrong), Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                progressBar?.isVisible = false
                recyclerView?.isVisible = false
                searchPlaceholder?.isVisible = true
                errorTitle?.text = getString(R.string.no_internet_title)
                errorDescription?.isVisible = true
                buttonRefresh?.isVisible = true
                isLastRequestFailed = true
                errorIcon?.setImageDrawable(getDrawable(R.drawable.ic_no_internet))
            }
        })
    }

    private fun displaySearchHistory() {
        val history = searchHistory?.getHistory()

        if (editTextSearch?.hasFocus() == true && editTextSearch?.text.isNullOrEmpty() && history?.isNotEmpty() == true) {
            searchTextViewForHistory?.isVisible = true
            buttonClearSearchHistory?.isVisible = true
            recyclerView?.isVisible = true
            trackAdapter?.setItems(history)
        } else {
            hideSearchHistory()
        }
    }

    private fun hideKeyboard() {
        if (editTextSearch != null) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                editTextSearch!!.windowToken, 0
            )
        }
    }

    private fun hideSearchHistory() {
        searchTextViewForHistory?.isVisible = false
        buttonClearSearchHistory?.isVisible = false
        recyclerView?.isVisible = false
    }

    private fun hideSearchResults() {
        recyclerView?.isVisible = false
        searchPlaceholder?.isVisible = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("EDIT_TEXT_CONTENT", editTextContent)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextContent = savedInstanceState.getString("EDIT_TEXT_CONTENT")
        editTextSearch?.setText(editTextContent)
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