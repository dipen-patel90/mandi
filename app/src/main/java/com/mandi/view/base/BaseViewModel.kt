package com.mandi.view.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}