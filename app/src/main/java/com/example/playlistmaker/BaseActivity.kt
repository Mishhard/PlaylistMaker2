package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    protected fun applySystemBarsPadding(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val defaultPadding = resources.getDimensionPixelSize(R.dimen.main_padding)

            v.setPadding(
                systemBars.left + defaultPadding,
                systemBars.top + defaultPadding,
                systemBars.right + defaultPadding,
                systemBars.bottom + defaultPadding
            )

            insets
        }
    }
}