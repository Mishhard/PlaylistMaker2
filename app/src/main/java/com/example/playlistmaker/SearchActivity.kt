package com.example.playlistmaker

import RetrofitClient
import TracksAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class SearchActivity : BaseActivity() {

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderContainer: View
    private lateinit var placeholderImage: ImageView
    private lateinit var tracksList: RecyclerView
    private lateinit var clearButton: View
    private lateinit var retryButton: Button
    private lateinit var backButton: ImageView
    private lateinit var searchHistoryContainer: ConstraintLayout
    private lateinit var clearHistoryButton: Button

    private val tracks = ArrayList<Track>()
    private val adapter = TracksAdapter()
    private val historyAdapter = TracksAdapter()
    private lateinit var searchHistory: SearchHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val rootView = findViewById<LinearLayout>(R.id.root_view)
        applySystemBarsPadding(rootView)

        searchHistory = SearchHistory(this)

        placeholderContainer = findViewById(R.id.placeholderContainer)
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.searchEditText)
        tracksList = findViewById(R.id.rvTracks)
        clearButton = findViewById(R.id.clearButton)
        retryButton = findViewById(R.id.retryButton)
        backButton = findViewById(R.id.backButton)
        searchHistoryContainer = findViewById(R.id.searchHistoryContainer)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)

        adapter.tracks = tracks
        tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksList.adapter = adapter

        historyAdapter.tracks = searchHistory.loadHistory()
        val historyRecyclerView = findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter

        setupListeners()

        loadSearchHistory()
    }

    private fun setupListeners() {
        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch()
                hideKeyboard()
                true
            } else {
                false
            }
        }

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        clearButton.setOnClickListener {
            queryInput.setText("")
            clearButton.visibility = View.GONE
            clearTracksList()
        }
        retryButton.setOnClickListener {
            performSearch()
        }
        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && queryInput.text.isEmpty()) {
                showSearchHistory()
            } else {
                hideSearchHistory()
            }
        }
        queryInput.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                    clearTracksList()
                    if (queryInput.hasFocus()) {
                        showSearchHistory()
                    } else {
                        hideSearchHistory()
                    }
                } else {
                    clearButton.visibility = View.VISIBLE
                    hideSearchHistory()
                }
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })
        adapter.setOnItemClickListener { track ->
            searchHistory.addTrack(track)
            loadSearchHistory()
            openPlayer(track)
        }
        historyAdapter.setOnItemClickListener { track ->
            searchHistory.addTrack(track)
            loadSearchHistory()
            openPlayer(track)
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            loadSearchHistory()
        }
    }

    private fun performSearch() {
        val query = queryInput.text.toString().trim()


        RetrofitClient.instance.search(query).enqueue(object :
            Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    tracks.clear()
                    response.body()?.results?.let {
                        tracks.addAll(it)
                    }
                    adapter.notifyDataSetChanged()

                    if (tracks.isEmpty()) {
                        showMessage(getString(R.string.nothing_found), true, isError = false)
                    } else {
                        showMessage("", false)
                    }
                } else {
                    showMessage(getString(R.string.connection_problem), true, isError = true)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showMessage(getString(R.string.connection_problem), true, isError = true)
            }
        })
    }

    private fun loadSearchHistory() {
        val history = searchHistory.loadHistory()
        if (history.isNotEmpty()) {
            historyAdapter.tracks = history
            historyAdapter.notifyDataSetChanged()
        } else {
            searchHistoryContainer.visibility = View.GONE
        }
    }

    private fun showSearchHistory() {
        if (searchHistory.loadHistory().isNotEmpty()) {
            searchHistoryContainer.visibility = View.VISIBLE
        }
    }

    private fun hideSearchHistory() {
        searchHistoryContainer.visibility = View.GONE
    }

    private fun showMessage(message: String, showPlaceholder: Boolean, isError: Boolean = false) {
        if (showPlaceholder) {
            placeholderContainer.visibility = View.VISIBLE
            placeholderMessage.text = message
            tracksList.visibility = View.GONE

            if (isError) {
                placeholderImage.setImageResource(R.drawable.track_connection_problem)
                placeholderMessage.text = getString(R.string.connection_problem)
                retryButton.visibility = View.VISIBLE
            } else {
                placeholderImage.setImageResource(R.drawable.track_nothing_found)
                placeholderMessage.text = getString(R.string.nothing_found)
                retryButton.visibility = View.GONE
            }
        } else {
            placeholderContainer.visibility = View.GONE
            tracksList.visibility = View.VISIBLE
            retryButton.visibility = View.GONE
        }
    }


    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(queryInput.windowToken, 0)
    }

    private fun clearTracksList() {
        tracks.clear()
        adapter.notifyDataSetChanged()
        showMessage("", false)
    }

    private fun openPlayer(track: Track) {
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra("track", track as Serializable)
        }
        startActivity(intent)
    }

}