package com.mandi.common

sealed class ToolbarConfig {
    class Show(val customTitle: String? = null) : ToolbarConfig()
    object Hide : ToolbarConfig()
}