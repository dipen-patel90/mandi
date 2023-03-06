package com.mandi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mandi.common.ToolbarConfig
import com.mandi.databinding.FragmentSellConfirmationBinding
import com.mandi.view.base.BaseFragment

class SellConfirmationFragment : BaseFragment() {

    private var _binding: FragmentSellConfirmationBinding? = null
    private val binding get() = _binding!!

    private val args: SellConfirmationFragmentArgs by navArgs()

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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateToSellingFragment()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // endregion

    // region - base class methods
    override fun initViews() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            sellerName = args.sellerName
            price = args.price
            weight = args.weight

            sellMoreBtn.setOnClickListener {
                navigateToSellingFragment()
            }
        }
    }

    override fun collectFlow() {
    }
    // endregion

    private fun navigateToSellingFragment() {
        findNavController().navigate(SellConfirmationFragmentDirections.actionSellConfirmationFragmentToSellingFragment())
    }
}