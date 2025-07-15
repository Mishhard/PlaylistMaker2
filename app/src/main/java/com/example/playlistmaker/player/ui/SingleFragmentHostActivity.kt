package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.CreatePlaylistFragment

class SingleFragmentHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_fragment_host)

        val fragmentTag = intent.getStringExtra("fragment")

        if (savedInstanceState == null) {
            val fragment = when (fragmentTag) {
                "createPlaylist" -> CreatePlaylistFragment()
                else -> throw IllegalArgumentException("Unknown fragment: $fragmentTag")
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}
