package com.example.playlistmaker.library.ui

import DifferentVerticalHorizontalDecoration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.common.PlaylistAdapter
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.data.dto.PlaylistDto
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance(): PlaylistsFragment = PlaylistsFragment()
    }
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by viewModel()
    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlaylistAdapter { playlist ->
            val bundle = bundleOf(PlaylistFragment.ARG_PLAYLIST_ID to playlist.id)
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_playlistFragment,
                bundle
            )
        }
        binding.rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlaylists.adapter = adapter

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_createPlaylistFragment)

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.playlists.collectLatest { playlists: List<PlaylistDto> ->
                if (playlists.isEmpty()) {
                    binding.emptyPlaceholder.visibility = View.VISIBLE
                    binding.rvPlaylists.visibility = View.GONE
                } else {
                    binding.emptyPlaceholder.visibility = View.GONE
                    binding.rvPlaylists.visibility = View.VISIBLE
                    adapter.submitList(playlists)
                }
            }
        }


        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.item_vertical_spacing)
        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.item_horizontal_spacing)

        binding.rvPlaylists.addItemDecoration(
            DifferentVerticalHorizontalDecoration(verticalSpacing, horizontalSpacing)
        )



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
