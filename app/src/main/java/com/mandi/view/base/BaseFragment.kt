package com.mandi.view.base

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mandi.common.ToolbarConfig
import com.mandi.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {
    abstract fun initViews()
    abstract fun collectFlow()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        collectFlow()
    }

    fun setToolbar(toolbarConfig: ToolbarConfig) {
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).setToolbar(toolbarConfig)
        }
    }

    fun <K> collectFlow(flow: SharedFlow<K>, function: (K) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect {
                    function.invoke(it)
                }
            }
        }
    }

    fun <K> collectFlow(flow: StateFlow<K>, function: (K) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect {
                    function.invoke(it)
                }
            }
        }
    }

    fun <K> collectFlow(flow: Flow<K>, function: (K) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect {
                    function.invoke(it)
                }
            }
        }
    }

    fun provideStringsToViewModel(viewModel: BaseViewModel, @StringRes vararg resources: Int) {
        resources.forEach {
            viewModel.putLocalString(it, getString(it))
        }
    }
}