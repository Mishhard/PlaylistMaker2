package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.BaseActivity
import com.example.playlistmaker.player.ui.models.TrackUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : BaseActivity() {

    private val viewModel: PlayerViewModel by viewModel()


    private lateinit var ivCover: ImageView
    private lateinit var ivPlay:  ImageView
    private lateinit var tvTimer: TextView
    private lateinit var ivBack:  ImageView

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
        ivPlay  = findViewById(R.id.play)
        tvTimer = findViewById(R.id.timer)
        ivBack  = findViewById(R.id.back_button)

        val root = findViewById<ConstraintLayout>(R.id.constraintRoot)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, sys.top, 0, sys.bottom)
            insets
        }

        Glide.with(this)
            .load(track.getFullCover())
            .transform(RoundedCorners(16))
            .placeholder(R.drawable.track_placeholder)
            .into(ivCover)
        findViewById<TextView>(R.id.track_name).text  = track.trackName
        findViewById<TextView>(R.id.artist_name).text = track.artistName
        findViewById<TextView>(R.id.duration_value).text = track.getFormattedTime()
        findViewById<TextView>(R.id.year_value).text     = track.getReleaseYear()
        findViewById<TextView>(R.id.genre_value).text    = track.primaryGenreName
        findViewById<TextView>(R.id.country_value).text  = track.country
        tvTimer.text = track.getFormattedTime()

        if (!track.collectionName.isNullOrEmpty()) {
            findViewById<TextView>(R.id.album_value).text = track.collectionName
            findViewById<TextView>(R.id.album_value).visibility = TextView.VISIBLE
        } else {
            findViewById<TextView>(R.id.album_value).visibility = TextView.GONE
        }

        viewModel.state.observe(this) { st ->
            when (st) {
                is PlayerState.Loading   -> {
                    ivPlay.isEnabled = false
                    ivPlay.isClickable = false
                }
                is PlayerState.Prepared  -> {
                    ivPlay.isEnabled = true
                    ivPlay.isClickable = true
                }
                is PlayerState.Playing -> {
                    ivPlay.setImageResource(R.drawable.pause)
                    tvTimer.text = formatTime(st.progress)
                }
                is PlayerState.Paused, is PlayerState.Completed -> {
                    ivPlay.setImageResource(R.drawable.play)
                    tvTimer.text = formatTime(st.progress)
                }
                is PlayerState.Completed -> ivPlay.setImageResource(R.drawable.play)
            }
        }

        viewModel.positionLiveData.observe(this) { ms ->
            tvTimer.text = formatTime(ms)
        }
        ivPlay.setOnClickListener {
            if (viewModel.isPlaying()) viewModel.pause()
            else viewModel.play()
        }
        ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        viewModel.prepare(track.previewUrl)
    }

    private fun formatTime(ms: Int): String =
        String.format("%02d:%02d", ms / 60000, (ms / 1000) % 60)

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}
