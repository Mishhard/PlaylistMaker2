package com.example.playlistmaker.utils

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.View

fun Int.dpToPx(context: Context): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()

fun Int.dpToPx(fragment: Fragment): Int =
    this.dpToPx(fragment.requireContext())

fun Int.dpToPx(view: View): Int =
    this.dpToPx(view.context)
