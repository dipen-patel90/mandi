package com.mandi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mandi.common.ToolbarConfig
import com.mandi.databinding.FragmentSellConfirmationBinding
import com.mandi.view.base.BaseFragment

class SellConfirmationFragment : BaseFragment() {

    private var _binding: FragmentSellConfirmationBinding? = null
    private val binding get() = _binding!!

    // region - life cycle methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(ToolbarConfig.Hide)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // endregion

    // region - base class methods
    override fun initViews() {
    }

    override fun collectFlow() {
    }
    // endregion
}