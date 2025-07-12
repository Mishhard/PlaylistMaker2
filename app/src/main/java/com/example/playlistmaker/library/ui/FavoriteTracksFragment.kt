package com.example.playlistmaker.library.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.player.domain.db.FavoriteTracksViewModel
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.common.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.R
import com.example.playlistmaker.library.presentation.FavoritesState
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.repeatOnLifecycle



class FavoriteTracksFragment : Fragment() {

    companion object {
        fun newInstance(): FavoriteTracksFragment {
            return FavoriteTracksFragment()
        }
    }

    private val viewModel: FavoriteTracksViewModel by viewModel()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val adapter = TracksAdapter().apply {
        setOnItemClickListener { trackUiModel ->
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra(PlayerActivity.EXTRA_TRACK, trackUiModel)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = adapter

        viewModel.loadFavorites(getString(R.string.there_are_no_favorite_tracks))

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when (state) {
                        is FavoritesState.Loading -> {
                            binding.rvFavorites.isVisible = false
                            binding.placeholderContainer.isVisible = false
                        }

                        is FavoritesState.Empty -> {
                            binding.rvFavorites.isVisible = false
                            binding.placeholderContainer.isVisible = true
                            binding.placeholderMessage.text = state.message
                        }

                        is FavoritesState.Content -> {
                            binding.rvFavorites.isVisible = true
                            binding.placeholderContainer.isVisible = false
                            adapter.tracks = ArrayList(state.tracks)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
