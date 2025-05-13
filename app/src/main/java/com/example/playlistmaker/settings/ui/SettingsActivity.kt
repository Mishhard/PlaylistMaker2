package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.activity.viewModels
import com.example.playlistmaker.R
import com.example.playlistmaker.common.BaseActivity
import com.example.playlistmaker.creator.Creator

class SettingsActivity : BaseActivity() {

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.provideFactory(
            Creator.getSettingsInteractor,
            Creator.saveSettingsInteractor
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val rootView: LinearLayout = findViewById(R.id.root_view)
        applySystemBarsPadding(rootView)

        val themeSwitcher: SwitchCompat = findViewById(R.id.themeSwitcher)
        val backButton: ImageView = findViewById(R.id.backButton)
        val shareButton: Button   = findViewById(R.id.shareButton)
        val supportButton: Button = findViewById(R.id.supportButton)
        val termsButton: Button   = findViewById(R.id.termsButton)

        viewModel.state.observe(this) { state ->
            when (state) {
                is SettingsState.Loading -> themeSwitcher.isEnabled = false
                is SettingsState.Content -> {
                    themeSwitcher.isEnabled = true
                    themeSwitcher.isChecked = state.darkThemeEnabled
                    AppCompatDelegate.setDefaultNightMode(
                        if (state.darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
                        else AppCompatDelegate.MODE_NIGHT_NO
                    )
                }
            }
        }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.onThemeToggled(checked)
        }

        backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        shareButton.setOnClickListener { shareApp() }
        supportButton.setOnClickListener { contactSupport() }
        termsButton.setOnClickListener { openUserTerms() }
    }

    private fun shareApp() {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.android_practicum_url))
        })
    }

    private fun contactSupport() {
        startActivity(Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:${getString(R.string.support_email)}")
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body))
        })
    }

    private fun openUserTerms() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_terms_url))))
    }
}