package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.presentation.ui.BaseActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Settings
import com.example.playlistmaker.creator.Creator

class SettingsActivity : BaseActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val rootView = findViewById<LinearLayout>(R.id.root_view)
        applySystemBarsPadding(rootView)

        Creator.init(this)

        // Инициализация интеракторов
        val getSettings = Creator.getSettingsInteractor
        val saveSettings = Creator.saveSettingsInteractor

        // Настройка переключателя темы
        val themeSwitcher = findViewById<SwitchCompat>(R.id.themeSwitcher)
        val current = getSettings.execute()
        themeSwitcher.isChecked = current.darkThemeEnabled

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            // Сохранение настроек
            saveSettings.execute(Settings(darkThemeEnabled = checked))
            // Применение режима темы
            AppCompatDelegate.setDefaultNightMode(
                if (checked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
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
