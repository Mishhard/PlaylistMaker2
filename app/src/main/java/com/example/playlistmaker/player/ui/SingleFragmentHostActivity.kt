package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.CreatePlaylistFragment

class SingleFragmentHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_fragment_host)

        val rootView = findViewById<FrameLayout>(R.id.fragment_container)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, sys.top, 0, sys.bottom)
            insets
        }
        val fragmentTag = intent.getStringExtra("fragment")

        if (savedInstanceState == null) {
            val fragment = when (fragmentTag) {
                "createPlaylist" -> {
                    val createPlaylistFragment = CreatePlaylistFragment()
                    createPlaylistFragment.arguments = Bundle().apply {
                        putString("source", intent.getStringExtra("source"))
                    }
                    createPlaylistFragment
                }
                else -> throw IllegalArgumentException("Unknown fragment: $fragmentTag")
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

    }
}
