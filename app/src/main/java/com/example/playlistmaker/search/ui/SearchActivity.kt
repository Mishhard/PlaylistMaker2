package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.api.GetSearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SaveSearchTermInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.creator.Creator.clearSearchHistoryInteractor
import com.example.playlistmaker.presentation.ui.TracksAdapter
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.presentation.ui.BaseActivity
import kotlinx.coroutines.launch

class SearchActivity : BaseActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var queryInput: EditText
    private lateinit var placeholderContainer: View
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var retryButton: Button
    private lateinit var tracksList: RecyclerView
    private lateinit var backButton: ImageView
    private lateinit var clearButton: View
    private lateinit var searchHistoryContainer: ConstraintLayout
    private lateinit var clearHistoryButton: Button

    private val adapter = TracksAdapter()
    private val historyAdapter = TracksAdapter()
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { performSearch() }
    private var isClickAllowed = true

    private lateinit var searchUseCase: SearchTracksInteractor
    private lateinit var getHistoryUseCase: GetSearchHistoryInteractor
    private lateinit var saveTermUseCase: SaveSearchTermInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Creator.init(this)

        searchUseCase = Creator.searchUseCase
        getHistoryUseCase = Creator.getSearchHistoryInteractor
        saveTermUseCase = Creator.saveSearchTermInteractor

        val rootView = findViewById<LinearLayout>(R.id.root_view)
        applySystemBarsPadding(rootView)
        bindViews()
        setupRecycler()
        setupListeners()
        loadSearchHistory()
    }

    private fun bindViews() {
        progressBar = findViewById(R.id.progressBar)
        placeholderContainer = findViewById(R.id.placeholderContainer)
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        retryButton = findViewById(R.id.retryButton)
        queryInput = findViewById(R.id.searchEditText)
        tracksList = findViewById(R.id.rvTracks)
        backButton = findViewById(R.id.backButton)
        clearButton = findViewById(R.id.clearButton)
        searchHistoryContainer = findViewById(R.id.searchHistoryContainer)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
    }

    private fun setupRecycler() {
        // Основной список результатов
        tracksList.layoutManager = LinearLayoutManager(this)
        tracksList.adapter = adapter
        adapter.setOnItemClickListener { track ->
            if (clickAllowed()) {
                saveTermUseCase.execute(track)
                openPlayer(track)
            }
        }
        // Список истории поиска
        val historyRecycler = findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter
        historyAdapter.setOnItemClickListener { track ->
            if (clickAllowed()) {
                saveTermUseCase.execute(track)
                loadSearchHistory()
                openPlayer(track)
            }
        }
    }

    private fun setupListeners() {
        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                scheduleSearch()
                hideKeyboard()
                true
            } else false
        }

        queryInput.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    hideContent()
                    clearButton.visibility = View.GONE
                    adapter.tracks.clear(); adapter.notifyDataSetChanged()
                    if (queryInput.hasFocus()) showHistory(getHistoryUseCase.execute()) else hideHistory()
                    handler.removeCallbacks(searchRunnable)
                } else {
                    clearButton.visibility = View.VISIBLE
                    hideHistory()
                    scheduleSearch()
                }
            }
        })

        retryButton.setOnClickListener { performSearch() }
        clearButton.setOnClickListener {
            queryInput.setText("")
            adapter.tracks.clear(); adapter.notifyDataSetChanged()
        }
        clearHistoryButton.setOnClickListener {
            clearSearchHistoryInteractor.execute()
            historyAdapter.tracks.clear(); historyAdapter.notifyDataSetChanged()
            hideHistory()
        }
        backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun scheduleSearch() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, 500)
    }

    private fun performSearch() {
        val term = queryInput.text.toString().trim()
        if (term.isEmpty()) return
        hideContent()
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val results = try {
                searchUseCase.execute(term)
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                showMessage(getString(R.string.connection_problem), true, isError = true)
                return@launch
            }
            progressBar.visibility = View.GONE
            if (results.isEmpty()) {
                showMessage(getString(R.string.nothing_found), true, isError = false)
            }
            else showResults(results)
        }
    }

    private fun loadSearchHistory() {
        showHistory(getHistoryUseCase.execute())
    }

    private fun showHistory(history: List<Track>) {
        if (history.isEmpty()) searchHistoryContainer.visibility = View.GONE
        else {
            searchHistoryContainer.visibility = View.VISIBLE
            historyAdapter.tracks = ArrayList(history); historyAdapter.notifyDataSetChanged()
        }
    }

    private fun hideHistory() {
        searchHistoryContainer.visibility = View.GONE
    }

    private fun showResults(list: List<Track>) {
        placeholderContainer.visibility = View.GONE
        tracksList.visibility = View.VISIBLE
        adapter.tracks = ArrayList(list); adapter.notifyDataSetChanged()
    }

    private fun clickAllowed(): Boolean {
        if (!isClickAllowed) return false
        isClickAllowed = false
        handler.postDelayed({ isClickAllowed = true }, 1000)
        return true
    }

    private fun openPlayer(track: Track) {
        startActivity(Intent(this, PlayerActivity::class.java).putExtra("track", track))
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(queryInput.windowToken, 0)
    }

    private fun hideContent() {
        placeholderContainer.visibility = View.GONE
        tracksList.visibility = View.GONE
        retryButton.visibility = View.GONE
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
            hideContent()
            tracksList.visibility = View.VISIBLE
        }
    }
}