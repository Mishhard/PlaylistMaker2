package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.common.BaseActivity
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl

class PlayerActivity : BaseActivity() {

    private val viewModel: PlayerViewModel by viewModels {
        PlayerViewModel.provideFactory(PlayerInteractorImpl())
    }

    private lateinit var ivCover: ImageView
    private lateinit var ivPlay: ImageView
    private lateinit var tvTimer: TextView
    private lateinit var tvName: TextView
    private lateinit var tvArtist: TextView
    private lateinit var tvAlbum: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvGenre: TextView
    private lateinit var tvCountry: TextView
    private lateinit var tvDuration: TextView
    private lateinit var ivBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val track = intent.getParcelableExtra<Track>("track") ?: return

        bindViews()
        applyInsets()

        populateMetadata(track)


        viewModel.state.observe(this) { state ->
            when (state) {
                is PlayerState.Loading -> {}
                is PlayerState.Prepared -> {}
                is PlayerState.Playing -> ivPlay.setImageResource(R.drawable.pause)
                is PlayerState.Paused  -> ivPlay.setImageResource(R.drawable.play)
                is PlayerState.Position -> tvTimer.text = formatTime(state.milliseconds)
            }
        }

        ivPlay.setOnClickListener {
            if (viewModel.isPlaying()) {
                viewModel.pause()
            } else {
                viewModel.play()
            }
        }


        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel.prepare(track.previewUrl)
    }

    private fun bindViews() {
        ivCover    = findViewById(R.id.track_cover)
        ivPlay     = findViewById(R.id.play)
        tvTimer    = findViewById(R.id.timer)
        tvName     = findViewById(R.id.track_name)
        tvArtist   = findViewById(R.id.artist_name)
        tvDuration = findViewById(R.id.duration_value)
        tvAlbum    = findViewById(R.id.album_value)
        tvYear     = findViewById(R.id.year_value)
        tvGenre    = findViewById(R.id.genre_value)
        tvCountry  = findViewById(R.id.country_value)
        ivBack     = findViewById(R.id.back_button)
    }

    private fun applyInsets() {
        val root: ConstraintLayout = findViewById(R.id.constraintRoot)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, sys.top, 0, sys.bottom)
            insets
        }
    }

    private fun populateMetadata(track: Track) {
        Glide.with(this)
            .load(track.getFullCover())
            .transform(RoundedCorners(16))
            .placeholder(R.drawable.track_placeholder)
            .into(ivCover)

        tvName.text     = track.trackName
        tvArtist.text   = track.artistName
        tvDuration.text = track.getFormattedTime()
        tvTimer.text    = track.getFormattedTime()

        if (!track.collectionName.isNullOrEmpty()) {
            tvAlbum.text       = track.collectionName
            tvAlbum.visibility = View.VISIBLE
        } else {
            tvAlbum.visibility = View.GONE
        }

        tvYear.text    = track.getReleaseYear()
        tvGenre.text   = track.primaryGenreName
        tvCountry.text = track.country
    }

    private fun formatTime(ms: Int): String =
        String.format("%02d:%02d", ms / 60000, (ms / 1000) % 60)

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}
