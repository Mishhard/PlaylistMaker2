package com.example.playlistmaker.player.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.BaseActivity
import com.example.playlistmaker.library.data.dto.PlaylistDto
import com.example.playlistmaker.player.ui.models.TrackUiModel
import com.example.playlistmaker.common.BottomSheetPlaylistsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.main.ui.MainActivity
import com.example.playlistmaker.utils.dpToPx

class PlayerActivity : BaseActivity() {

    companion object {
        const val EXTRA_TRACK = "track"
    }

    private val viewModel: PlayerViewModel by viewModel()

    private lateinit var ivCover: ImageView
    private lateinit var ivPlay: ImageView
    private lateinit var tvTimer: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivLike: ImageView

    private lateinit var overlay: View
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var playlistsAdapter: BottomSheetPlaylistsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val uiModel: TrackUiModel? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("track", TrackUiModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<TrackUiModel>("track")
        }
        val track = uiModel?.toDomain() ?: return

        ivCover = findViewById(R.id.track_cover)
        ivPlay = findViewById(R.id.play)
        tvTimer = findViewById(R.id.timer)
        ivBack = findViewById(R.id.back_button)
        ivLike = findViewById(R.id.like)

        overlay = findViewById(R.id.overlay)
        val bottomSheet = findViewById<LinearLayout>(R.id.bottomSheetRoot)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                overlay.visibility =
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })

        overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        val root = findViewById< CoordinatorLayout>(R.id.coordinatorRoot)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, sys.top, 0, sys.bottom)
            insets
        }

        Glide.with(this)
            .load(track.getFullCover())
            .transform(RoundedCorners(8.dpToPx(this))
            )
            .placeholder(R.drawable.track_placeholder)
            .into(ivCover)

        findViewById<TextView>(R.id.track_name).text = track.trackName
        findViewById<TextView>(R.id.artist_name).text = track.artistName
        findViewById<TextView>(R.id.duration_value).text = track.getFormattedTime()
        findViewById<TextView>(R.id.year_value).text = track.getReleaseYear()
        findViewById<TextView>(R.id.genre_value).text = track.primaryGenreName
        findViewById<TextView>(R.id.country_value).text = track.country

        tvTimer.text = track.getFormattedTime()

        if (!track.collectionName.isNullOrEmpty()) {
            findViewById<TextView>(R.id.album_value).text = track.collectionName
            findViewById<TextView>(R.id.album_value).visibility = View.VISIBLE
        } else {
            findViewById<TextView>(R.id.album_value).visibility = View.GONE
        }

        viewModel.updateCurrentTrack(track)
        viewModel.state.observe(this) { st ->
            when (st) {
                is PlayerState.Liked -> ivLike.setImageResource(R.drawable.like_active)
                is PlayerState.NotLiked -> ivLike.setImageResource(R.drawable.like)
                is PlayerState.Loading -> ivPlay.isEnabled = false
                is PlayerState.Prepared -> ivPlay.isEnabled = true
                is PlayerState.Playing -> {
                    ivPlay.setImageResource(R.drawable.pause)
                    tvTimer.text = formatTime(st.progress)
                }
                is PlayerState.Paused, is PlayerState.Completed -> {
                    ivPlay.setImageResource(R.drawable.play)
                    tvTimer.text = formatTime(st.progress)
                }
            }
        }
        viewModel.positionLiveData.observe(this) { ms ->
            tvTimer.text = formatTime(ms)
        }

        ivPlay.setOnClickListener {
            if (viewModel.isPlaying()) viewModel.pause()
            else viewModel.play()
        }
        ivLike.setOnClickListener { viewModel.toggleFavorite() }
        ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        viewModel.prepare(track.previewUrl)

        playlistsAdapter = BottomSheetPlaylistsAdapter { playlist ->
            addTrackToPlaylist(playlist)
        }
        val rvBottom = findViewById<RecyclerView>(R.id.rvPlaylistsBottomSheet)
        rvBottom.layoutManager = LinearLayoutManager(this)
        rvBottom.adapter = playlistsAdapter

        viewModel.playlists.observe(this) {
            playlistsAdapter.submitList(it)
        }

        findViewById<Button>(R.id.button_new_playlist).setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            val intent = Intent(this, SingleFragmentHostActivity::class.java).apply {
                putExtra("fragment", "createPlaylist")
                putExtra("source", "player")
            }
            startActivity(intent)
        }



        findViewById<ImageView>(R.id.add_button).setOnClickListener {
            viewModel.loadPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    private fun formatTime(ms: Int): String =
        String.format("%02d:%02d", ms / 60000, (ms / 1000) % 60)

    private fun addTrackToPlaylist(playlist: PlaylistDto) {
        viewModel.addTrackToPlaylist(playlist) { added ->
            val message = if (added) {
                getString(R.string.added_to_playlist, playlist.name)
            } else {
                getString(R.string.track_already_in_playlist, playlist.name)
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }







}
