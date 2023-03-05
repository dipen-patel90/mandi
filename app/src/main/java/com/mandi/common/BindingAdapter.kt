package com.mandi.common

import android.widget.TextView
import androidx.annotation.DrawableRes

object BindingAdapter {

    @JvmStatic
    @androidx.databinding.BindingAdapter(
        value = ["bindDrawableEndCompat"],
    )
    fun bindDrawableEndCompat(textView: TextView, @DrawableRes bindDrawableEndCompat: Int) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            bindDrawableEndCompat,
            0
        )
    }
}