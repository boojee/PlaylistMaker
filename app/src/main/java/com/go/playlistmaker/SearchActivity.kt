package com.go.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private var editTextSearch: EditText? = null
    private var buttonClear: ImageView? = null
    private var editTextContent: String? = null
    private var buttonBackward: ImageView? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextSearch = findViewById(R.id.edit_text_search)
        buttonClear = findViewById(R.id.button_clear)
        buttonBackward = findViewById(R.id.button_back)
        recyclerView = findViewById(R.id.recycler_view_search)

        initRecyclerView()

        buttonBackward?.setOnClickListener {
            finish()
        }

        buttonClear?.isVisible = !editTextSearch?.text.isNullOrEmpty()

        buttonClear?.setOnClickListener {
            editTextSearch?.setText("")
            if (editTextSearch != null) {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(editTextSearch!!.windowToken, 0)
            }
        }

        val simpleEditTextSearch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextContent = s?.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                buttonClear?.isVisible = !s.isNullOrEmpty()
            }
        }

        editTextSearch?.addTextChangedListener(simpleEditTextSearch)

        if (savedInstanceState != null) {
            editTextContent = savedInstanceState.getString("EDIT_TEXT_CONTENT")
            editTextSearch?.setText(editTextContent)
        }

    }

    private fun initRecyclerView() {
        recyclerView?.layoutManager = LinearLayoutManager(this)
        val trackAdapter = TrackAdapter(MockList.createTrackList())
        recyclerView?.adapter = trackAdapter
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
}