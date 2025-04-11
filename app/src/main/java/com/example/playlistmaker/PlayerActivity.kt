package com.example.playlistmaker

import android.app.usage.NetworkStats.Bucket.STATE_DEFAULT
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : BaseActivity() {

    private lateinit var track: Track
    private lateinit var playButton: ImageView
    private lateinit var timer: TextView
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            val position = mediaPlayer?.currentPosition ?: 0
            timer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(position)
            handler.postDelayed(this, 300)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val rootView = findViewById<ScrollView>(R.id.root_view)
        applySystemBarsPadding(rootView)

        val backButton: ImageView = findViewById(R.id.back_button)
        val trackCover: ImageView = findViewById(R.id.track_cover)
        val trackName: TextView = findViewById(R.id.track_name)
        val artistName: TextView = findViewById(R.id.artist_name)
        val timer: TextView = findViewById(R.id.timer)
        val albumName: TextView = findViewById(R.id.album_value)
        val releaseYear: TextView = findViewById(R.id.year_value)
        val genre: TextView = findViewById(R.id.genre_value)
        val country: TextView = findViewById(R.id.country_value)
        val trackDuration: TextView = findViewById(R.id.duration_value)
        playButton = findViewById(R.id.play)
        timer = findViewById(R.id.timer)

        preparePlayer()

        playButton.setOnClickListener {
            playbackControl()
        }

        track = intent.getSerializableExtra("track") as? Track ?: return

        Glide.with(this)
            .load(track.getFullCover())
            .transform(RoundedCorners(16))
            .placeholder(R.drawable.track_placeholder)
            .into(trackCover)

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = track.getFormattedTime()
        releaseYear.text = track.getReleaseYear()
        genre.text = track.primaryGenreName
        country.text = track.country
        timer.text = track.getFormattedTime()


        if (!track.collectionName.isNullOrEmpty()) {
            albumName.text = track.collectionName
            albumName.visibility = TextView.VISIBLE
        } else {
            albumName.visibility = TextView.GONE
        }

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun onPause() {
        super.onPause()
        if (playerState == STATE_PLAYING) pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimerRunnable)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun preparePlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playButton.isEnabled = true
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                playButton.setImageResource(R.drawable.play)
                playerState = STATE_PREPARED
                timer.text = "00:00"
                handler.removeCallbacks(updateTimerRunnable)
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        playButton.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        handler.post(updateTimerRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer?.pause()
        playButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(updateTimerRunnable)
    }


    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

}
