package com.mandi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Village(val name: String, val price: Double) : Parcelable
