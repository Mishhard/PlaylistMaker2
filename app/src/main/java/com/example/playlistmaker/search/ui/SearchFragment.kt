package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.common.TracksAdapter
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.domain.models.Track as DomainTrack
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.player.ui.models.TrackUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private val resultsAdapter = TracksAdapter(
        onTrackClick = { trackUiModel ->
            viewModel.saveTrack(trackUiModel.toDomain())
            openPlayer(trackUiModel)
        }
    )

    private val historyAdapter = TracksAdapter(
        onTrackClick = { trackUiModel ->
            viewModel.onTrackClicked(trackUiModel.toDomain())
            openPlayer(trackUiModel)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTracks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resultsAdapter
        }
        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Loading -> showLoading()
                is SearchState.Content -> showResults(
                    state.tracks.map { TrackUiModel.fromDomain(it, isFavorite = false) }
                )

                is SearchState.History -> showHistory(
                    state.tracks.map { TrackUiModel.fromDomain(it, isFavorite = false) }
                )

                is SearchState.Empty   -> showEmpty(state.message)
                is SearchState.Error   -> showError(state.message)
            }
        }

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            binding.clearButton.isVisible = !text.isNullOrEmpty()
            viewModel.onQueryChanged(text.toString())
        }
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.onQueryChanged(binding.searchEditText.text.toString())
                true
            } else false
        }

        binding.clearButton.setOnClickListener {
            binding.searchEditText.text?.clear()
            resultsAdapter.submitList(emptyList())
            viewModel.onQueryChanged("")
        }
        binding.retryButton.setOnClickListener {
            viewModel.onQueryChanged(binding.searchEditText.text.toString())
        }
        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    private fun openPlayer(track: TrackUiModel) {
        startActivity(Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.EXTRA_TRACK, track)
        })
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.placeholderContainer.isVisible = false
        binding.rvTracks.isVisible = false
        binding.searchHistoryContainer.isVisible = false
    }

    private fun showResults(list: List<TrackUiModel>) {
        binding.progressBar.isVisible = false
        binding.placeholderContainer.isVisible = false
        binding.searchHistoryContainer.isVisible = false
        binding.rvTracks.isVisible = true
        resultsAdapter.submitList(list)
    }


    private fun showHistory(list: List<TrackUiModel>) {
        binding.progressBar.isVisible = false
        binding.placeholderContainer.isVisible = false
        binding.rvTracks.isVisible = false
        binding.searchHistoryContainer.isVisible = list.isNotEmpty()
        historyAdapter.submitList(list)
    }

    private fun showEmpty(message: String) {
        binding.progressBar.isVisible = false
        binding.searchHistoryContainer.isVisible = false
        binding.rvTracks.isVisible = false
        binding.placeholderContainer.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.track_nothing_found)
        binding.placeholderMessage.text = message
        binding.retryButton.isVisible = false
    }

    private fun showError(message: String) {
        binding.progressBar.isVisible = false
        binding.searchHistoryContainer.isVisible = false
        binding.rvTracks.isVisible = false
        binding.placeholderContainer.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.track_connection_problem)
        binding.placeholderMessage.text = message
        binding.retryButton.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}