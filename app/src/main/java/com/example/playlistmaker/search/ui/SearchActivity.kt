package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Button
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.presentation.ui.BaseActivity
import com.example.playlistmaker.presentation.ui.TracksAdapter
import com.example.playlistmaker.creator.Creator

class SearchActivity : BaseActivity() {

    private lateinit var rootView: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var placeholderContainer: View
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var retryButton: Button
    private lateinit var rvTracks: RecyclerView
    private lateinit var rvHistory: RecyclerView
    private lateinit var etQuery: EditText
    private lateinit var btnClearHistory: Button
    private lateinit var clearButton: View
    private lateinit var backButton: ImageView

    private val resultsAdapter = TracksAdapter().apply {
        setOnItemClickListener { track -> openPlayer(track) }
    }
    private val historyAdapter = TracksAdapter().apply {
        setOnItemClickListener { track -> openPlayer(track) }
    }

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.provideFactory(
            Creator.searchUseCase,
            Creator.getSearchHistoryInteractor,
            Creator.saveSearchTermInteractor,
            Creator.clearSearchHistoryInteractor
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Creator.init(this)

        rootView = findViewById(R.id.root_view)
        applySystemBarsPadding(rootView)
        bindViews()
        setupRecycler()
        setupBindings()
    }

    private fun bindViews() {
        progressBar = findViewById(R.id.progressBar)
        placeholderContainer = findViewById(R.id.placeholderContainer)
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        retryButton = findViewById(R.id.retryButton)
        rvTracks = findViewById(R.id.rvTracks)
        rvHistory = findViewById(R.id.historyRecyclerView)
        etQuery = findViewById(R.id.searchEditText)
        btnClearHistory = findViewById(R.id.clearHistoryButton)
        clearButton = findViewById(R.id.clearButton)
        backButton = findViewById(R.id.backButton)
    }

    private fun setupRecycler() {
        rvTracks.layoutManager = LinearLayoutManager(this)
        rvTracks.adapter = resultsAdapter

        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = historyAdapter
    }

    private fun setupBindings() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is SearchState.Loading -> showLoading()
                is SearchState.Content -> showResults(state.tracks)
                is SearchState.Empty   -> showEmpty(state.message)
                is SearchState.History -> showHistory(state.tracks)
                is SearchState.Error   -> showError(state.message)
            }
        }

        etQuery.doOnTextChanged { text, _, _, _ ->
            viewModel.onQueryChanged(text.toString())
        }

        etQuery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.onQueryChanged(etQuery.text.toString())
                true
            } else false
        }

        clearButton.setOnClickListener {
            etQuery.setText("")
            resultsAdapter.tracks.clear()
            resultsAdapter.notifyDataSetChanged()
        }

        retryButton.setOnClickListener {
            viewModel.onQueryChanged(etQuery.text.toString())
        }

        btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun openPlayer(track: Track) {
        startActivity(Intent(this, PlayerActivity::class.java).apply {
            putExtra("track", track)
        })
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        placeholderContainer.visibility = View.GONE
        rvTracks.visibility = View.GONE
        rvHistory.visibility = View.GONE
    }

    private fun showResults(list: List<Track>) {
        progressBar.visibility = View.GONE
        placeholderContainer.visibility = View.GONE
        rvHistory.visibility = View.GONE
        rvTracks.visibility = View.VISIBLE
        resultsAdapter.tracks = ArrayList(list)
        resultsAdapter.notifyDataSetChanged()
    }

    private fun showHistory(list: List<Track>) {
        progressBar.visibility = View.GONE
        placeholderContainer.visibility = View.GONE
        rvTracks.visibility = View.GONE
        rvHistory.visibility = View.VISIBLE
        historyAdapter.tracks = ArrayList(list)
        historyAdapter.notifyDataSetChanged()
    }

    private fun showEmpty(message: String) {
        progressBar.visibility = View.GONE
        placeholderContainer.visibility = View.VISIBLE
        placeholderMessage.text = message
        rvTracks.visibility = View.GONE
        rvHistory.visibility = View.GONE
        retryButton.visibility = View.GONE
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        placeholderContainer.visibility = View.VISIBLE
        placeholderMessage.text = message
        rvTracks.visibility = View.GONE
        rvHistory.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
    }
}