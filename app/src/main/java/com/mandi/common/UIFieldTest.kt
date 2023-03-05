package com.mandi.common

import com.mandi.extention.empty
import kotlinx.coroutines.flow.MutableStateFlow

data class UIFieldTest(
    val state: String = String.empty(),
    var hasError: Boolean = false,
    var errorMessage: String = String.empty()
)