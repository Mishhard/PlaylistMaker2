package com.example.playlistmaker

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

class PlayerActivity : BaseActivity() {

    private lateinit var track: Track

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
}
