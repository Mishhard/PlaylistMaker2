package com.example.playlistmaker

import TracksAdapter
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : BaseActivity() {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderContainer: View
    private lateinit var placeholderImage: ImageView
    private lateinit var tracksList: RecyclerView
    private lateinit var clearButton: View
    private lateinit var retryButton: Button

    private val tracks = ArrayList<Track>()
    private val adapter = TracksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val rootView = findViewById<LinearLayout>(R.id.root_view)
        applySystemBarsPadding(rootView)

        placeholderContainer = findViewById(R.id.placeholderContainer)
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.searchEditText)
        tracksList = findViewById(R.id.rvTracks)
        clearButton = findViewById(R.id.clearButton)
        retryButton = findViewById(R.id.retryButton)

        adapter.tracks = tracks
        tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksList.adapter = adapter

        setupListeners()
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


        clearButton.setOnClickListener {
            queryInput.setText("")
            clearButton.visibility = View.GONE
            clearTracksList()
        }
        retryButton.setOnClickListener {
            performSearch()
        }

        queryInput.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if (s.isNullOrEmpty())
                    {
                        clearButton.visibility = View.GONE
                        clearTracksList()
                    }
                    else clearButton.visibility = View.VISIBLE
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun performSearch() {
        val query = queryInput.text.toString().trim()


        itunesService.search(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
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
}
