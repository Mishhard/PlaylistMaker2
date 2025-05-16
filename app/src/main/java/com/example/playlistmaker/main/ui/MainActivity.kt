package com.example.playlistmaker.main.ui

import android.os.Bundle
import android.widget.LinearLayout
import com.example.playlistmaker.common.BaseActivity
import com.example.playlistmaker.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val rootView = findViewById<LinearLayout>(R.id.root_view)
        applySystemBarsPadding(rootView)

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController)

        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
}