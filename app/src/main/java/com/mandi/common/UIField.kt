package com.mandi.common

import com.mandi.extention.empty
import kotlinx.coroutines.flow.MutableStateFlow

data class UIField(
    val state: MutableStateFlow<String> = MutableStateFlow(String.empty()),
    var hasError: Boolean = false,
    var errorMessage: String = String.empty()
)