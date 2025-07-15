package com.example.playlistmaker.main.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.playlistmaker.common.BaseActivity
import com.example.playlistmaker.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.setupWithNavController

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val rootView = findViewById<LinearLayout>(R.id.root_view)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, sys.top, 0, sys.bottom)
            insets
        }

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val hideBottomNavFragments = setOf(
                R.id.createPlaylistFragment
            )

            bottomNav.visibility = if (destination.id in hideBottomNavFragments) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        val openCreate = intent.getBooleanExtra("openCreatePlaylist", false)
        val source = intent.getStringExtra("source")

        if (openCreate) {
            val bundle = Bundle().apply { putString("source", source) }
            navController.navigate(R.id.createPlaylistFragment, bundle)
        }

    }

    override fun onSupportNavigateUp(): Boolean =
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment)
            .navController
            .navigateUp() || super.onSupportNavigateUp()
}