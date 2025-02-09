package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : BaseActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val rootView = findViewById<LinearLayout>(R.id.root_view)
        applySystemBarsPadding(rootView)

        val themeSwitcher = findViewById<SwitchCompat>(R.id.themeSwitcher)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val shareButton = findViewById<Button>(R.id.shareButton)
        shareButton.setOnClickListener {
            shareApp()
        }

        val supportButton = findViewById<Button>(R.id.supportButton)
        supportButton.setOnClickListener {
            contactSupport()
        }

        val termsButton = findViewById<Button>(R.id.termsButton)
        termsButton.setOnClickListener {
            openUserTerms()
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.android_practicum_url))
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }

    private fun contactSupport() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:${getString(R.string.support_email)}")
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body))
        }
        startActivity(Intent.createChooser(emailIntent, null))
    }

    private fun openUserTerms() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_terms_url)))
        startActivity(browserIntent)
    }
}
