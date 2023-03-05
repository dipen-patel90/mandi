package com.mandi.view

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.mandi.R
import com.mandi.common.ToolbarConfig
import com.mandi.databinding.FragmentSellingBinding
import com.mandi.model.Seller
import com.mandi.view.base.BaseFragment
import com.mandi.view.dialog.SingleSelectionBottomSheet
import com.mandi.viewmodel.SellingViewModel

class SellingFragment : BaseFragment() {

    private var _binding: FragmentSellingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellingViewModel by viewModels()

    private val sellerAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line, arrayListOf<Seller>()
        )
    }

    // region - life cycle methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(ToolbarConfig.Show(getString(R.string.selling_fragment)))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // endregion

    // region - base class methods
    override fun initViews() {
        binding.apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner

            grossWeight.editText.inputType = InputType.TYPE_CLASS_NUMBER
            grossWeight.editText.filters = arrayOf(InputFilter.LengthFilter(9))
            villageName.editText.inputType = InputType.TYPE_NULL
            villageName.editText.focusable = View.NOT_FOCUSABLE

            // Once user select the seller from the list keep the selected Item in viewmodel
            sellerName.editText.setOnItemClickListener { adapterView, _, selectedIndex, _ ->
                val seller = adapterView.getItemAtPosition(selectedIndex) as Seller
                viewModel.setSelectedSeller(seller)
            }
            sellerName.editText.doAfterTextChanged {
                it?.toString()?.let {
                    viewModel.setOrClearSeller(it)
                }
            }

            loyaltyCardIdentifier.editText.doAfterTextChanged {
                it?.toString()?.let {
                    viewModel.setSellerName(it)
                }
            }

            villageName.editText.setOnClickListener {
                SingleSelectionBottomSheet.newInstance(
                    childFragmentManager,
                    getString(R.string.select_village),
                    viewModel.villageList.value
                )
            }
        }
    }

    override fun collectFlow() {
        binding.apply {
            collectFlow(viewModel.sellerList) {
                sellerAdapter.addAll(it)
                sellerName.editText.setAdapter(sellerAdapter)
            }

            // Empty collector to trigger calculate 'combine' call
            collectFlow(viewModel.calculate) {}
        }
    }
    // endregion
}