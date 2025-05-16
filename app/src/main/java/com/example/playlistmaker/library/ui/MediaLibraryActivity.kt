package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.widget.LinearLayout
import com.example.playlistmaker.R
import com.example.playlistmaker.common.BaseActivity
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : BaseActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applySystemBarsPadding(binding.rootView)

        binding.viewPager.adapter = MediaLibraryPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Избранные треки"
                1 -> "Плейлисты"
                else -> null
            }
        }.attach()

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
