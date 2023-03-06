package com.mandi.view.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandi.extention.empty
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _progressCount = MutableStateFlow(0)
    val progressCount = _progressCount.asStateFlow()

    fun startLoading() {
        _progressCount.update { it + 1 }
    }

    fun stopLoading() {
        _progressCount.update { it - 1 }
    }

    fun launchWithViewModelScope(
        call: suspend () -> Unit,
        exceptionCallback: (exception: Throwable) -> Unit
    ) {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            exceptionCallback(exception)
        }) {
            call.invoke()
        }
    }

    // For keeping the required string values
    private val stringResources = hashMapOf<Int, String>()

    // ViewModel can access string value using this method
    // Before viewModel use this method to get string we need to put string from respective fragment/activity using "putLocalString" method
    fun getLocalString(resourceId: Int): String {
        return stringResources.getOrDefault(resourceId, String.empty())
    }

    // Provide required string value to viewModel using this function
    fun putLocalString(resourceId: Int, stringValue: String) {
        stringResources[resourceId] = stringValue
    }
}