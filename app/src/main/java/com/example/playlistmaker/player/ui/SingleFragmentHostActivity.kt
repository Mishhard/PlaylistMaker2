package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.CreatePlaylistFragment

class SingleFragmentHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_single_fragment_host)
        if (savedInstanceState == null) {
            val tag = intent.getStringExtra("fragment")
            val fragment = when (tag) {
                "createPlaylist" -> CreatePlaylistFragment().apply {
                    arguments = Bundle().apply {
                        putString("source", intent.getStringExtra("source"))
                    }
                }
                else -> throw IllegalArgumentException("Unknown fragment: $tag")
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

}
