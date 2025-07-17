package com.example.playlistmaker.library.ui

import android.R.attr.track
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.TracksAdapter
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.player.ui.models.TrackUiModel
import com.example.playlistmaker.utils.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistDetailsViewModel by viewModel()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var tracksAdapter: TracksAdapter

    private var playlistId: Long = -1

    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlistId = arguments?.getLong(ARG_PLAYLIST_ID) ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracksAdapter = TracksAdapter(
            onTrackClick = { track -> openPlayer(track) },
            onTrackLongClick = { track -> showDeleteTrackDialog(track) },
            roundCorners = false
        )

        binding.playlistTracksRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tracksAdapter
        }

        binding.playlistTracksRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tracksAdapter
        }

        setupBottomSheets()
        setupMenuActions()

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            binding.playlistTitle.text = playlist.name
            binding.playlistDescription.text = playlist.description ?: ""
            binding.menuPlaylistName.text = playlist.name

            if (playlist.imagePath != null) {
                Glide.with(this)
                    .load(playlist.imagePath)
                    .placeholder(R.drawable.track_placeholder)
                    .into(binding.playlistCover)

                Glide.with(this)
                    .load(playlist.imagePath)
                    .transform(CenterCrop(), RoundedCorners(8.dpToPx(this)))
                    .placeholder(R.drawable.track_placeholder)
                    .into(binding.menuCoverImage)
            } else {
                binding.playlistCover.setImageResource(R.drawable.track_placeholder)
                binding.menuCoverImage.setImageResource(R.drawable.track_placeholder)
            }
        }

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            if (tracks.isEmpty()) {
                binding.emptyMessage.visibility = View.VISIBLE
                binding.playlistTracksRecyclerView.visibility = View.GONE
            } else {
                binding.emptyMessage.visibility = View.GONE
                binding.playlistTracksRecyclerView.visibility = View.VISIBLE
            }

            tracksAdapter.submitList(tracks)

            val count = tracks.size
            val countText = resources.getQuantityString(R.plurals.track_count, count, count)
            val totalDuration = calculateTotalDuration(tracks)

            binding.playlistTracksInfo.text = "$countText · $totalDuration"
            binding.menuPlaylistTrackCount.text = countText
        }

        viewModel.loadPlaylist(playlistId)
    }

    private fun setupBottomSheets() {
        val bottomSheetTracks = binding.bottomSheetTracks
        BottomSheetBehavior.from(bottomSheetTracks).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.visibility =
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                _binding?.overlay?.alpha = slideOffset
            }
        }
        bottomSheetBehaviorMenu.addBottomSheetCallback(bottomSheetCallback)
    }

    private fun setupMenuActions() {
        binding.menuButton.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.shareButton.setOnClickListener {
            sharePlaylist()
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.menuItemShare.setOnClickListener {
            sharePlaylist()
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.menuItemEdit.setOnClickListener {
            editPlaylist()
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.menuItemDelete.setOnClickListener {
            confirmDeletePlaylist()
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun confirmDeletePlaylist() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_playlist)
            .setMessage(R.string.delete_playlist_confirm)
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deletePlaylist(playlistId) {
                    findNavController().popBackStack()
                }
            }
            .show()
    }

    private fun editPlaylist() {
        val bundle = bundleOf("playlist_id" to playlistId)
        findNavController().navigate(
            R.id.action_playlistFragment_to_createPlaylistFragment,
            bundle
        )
    }

    private fun sharePlaylist() {
        val playlist = viewModel.playlist.value
        val tracks = viewModel.tracks.value ?: emptyList()

        if (playlist == null || tracks.isEmpty()) {
            Toast.makeText(requireContext(), R.string.empty_playlist_error, Toast.LENGTH_SHORT).show()
            return
        }

        val shareText = buildString {
            append(getString(R.string.playlist, playlist.name))
            if (!playlist.description.isNullOrBlank()) {
                append("\n${playlist.description}")
            }
            append("\n\n")
            val trackCountText = resources.getQuantityString(
                R.plurals.track_count,
                tracks.size,
                tracks.size
            )
            append("$trackCountText:\n\n")


            tracks.forEachIndexed { index, track ->
                append("${index + 1}. ${track.artistName} - ${track.trackName} (${track.getFormattedTime()})\n")
            }
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(shareIntent, ""))
    }

    override fun onDestroyView() {
        if (::bottomSheetBehaviorMenu.isInitialized && ::bottomSheetCallback.isInitialized) {
            bottomSheetBehaviorMenu.removeBottomSheetCallback(bottomSheetCallback)
        }
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val ARG_PLAYLIST_ID = "playlist_id"
    }

    private fun calculateTotalDuration(tracks: List<TrackUiModel>): String {
        val totalMillis = tracks.sumOf { it.trackTimeMillis ?: 0 }
        val totalMinutes = totalMillis / 60000
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return if (hours > 0) {
            getString(R.string.hours_minutes, hours, minutes)
        } else {
            resources.getQuantityString(R.plurals.minutes, minutes.toInt(), minutes)
        }
    }


    private fun openPlayer(track: TrackUiModel) {
        val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.EXTRA_TRACK, track)
        }
        startActivity(intent)
    }

    private fun showDeleteTrackDialog(track: TrackUiModel) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_track_title)
            .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.removeTrackFromPlaylist(playlistId, track.trackId)
            }
            .show()
    }


}
