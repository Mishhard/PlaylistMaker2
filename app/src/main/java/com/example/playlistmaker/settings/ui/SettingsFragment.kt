package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_settings, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val themeSwitcher: SwitchCompat = view.findViewById(R.id.themeSwitcher)
        val shareButton: Button      = view.findViewById(R.id.shareButton)
        val supportButton: Button    = view.findViewById(R.id.supportButton)
        val termsButton: Button      = view.findViewById(R.id.termsButton)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SettingsState.Loading -> themeSwitcher.isEnabled = false
                is SettingsState.Content -> {
                    themeSwitcher.isEnabled = true
                    themeSwitcher.isChecked = state.darkThemeEnabled
                    AppCompatDelegate.setDefaultNightMode(
                        if (state.darkThemeEnabled)
                            AppCompatDelegate.MODE_NIGHT_YES
                        else
                            AppCompatDelegate.MODE_NIGHT_NO
                    )
                }
            }
        }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.onThemeToggled(checked)
        }

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
        startActivity(Intent(Intent.ACTION_VIEW,
            Uri.parse(getString(R.string.user_terms_url))))
    }
}