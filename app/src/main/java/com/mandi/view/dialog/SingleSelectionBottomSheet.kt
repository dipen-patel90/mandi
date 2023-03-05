package com.mandi.view.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.mandi.common.BottomSheetConfig
import com.mandi.databinding.BottomSheetSingleSelectionBinding
import com.mandi.extention.parcelableArrayList
import com.mandi.model.Village
import com.mandi.view.base.BaseBottomSheet
import com.mandi.viewmodel.SellingViewModel


class SingleSelectionBottomSheet :
    BaseBottomSheet(BottomSheetConfig.FullTransparentTop(true, 10)) {

    private var _binding: BottomSheetSingleSelectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellingViewModel by viewModels({ requireParentFragment() })

    private var itemList = arrayListOf<Village>()

    private val adapter by lazy {
        SingleSelectionAdapter {
            viewModel.setSelectedVillage(it)
            dismiss()
        }
    }

    companion object {
        private const val SINGLE_SELECTION_DIALOG_TAG = "SINGLE_SELECTION_DIALOG_TAG"

        private const val SINGLE_SELECTION_TITLE = "SINGLE_SELECTION_TITLE"
        private const val SINGLE_SELECTION_LIST = "SINGLE_SELECTION_LIST"

        @JvmStatic
        fun newInstance(
            childFragmentManager: FragmentManager,
            title: String,
            itemList: List<Village>
        ): SingleSelectionBottomSheet {
            return SingleSelectionBottomSheet().apply {
                arguments = bundleOf(
                    SINGLE_SELECTION_TITLE to title,
                    SINGLE_SELECTION_LIST to itemList.map { it.copy() },
                )
                show(childFragmentManager, SINGLE_SELECTION_DIALOG_TAG)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSingleSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            arguments?.apply {
                title = getString(SINGLE_SELECTION_TITLE)

                parcelableArrayList<Village>(SINGLE_SELECTION_LIST)?.let {
                    itemList = it
                    adapter.setData(itemList)
                    rvList.adapter = adapter
                }
            }

            searchText.doAfterTextChanged { input ->
                val searchText = input.toString()

                if (searchText.isEmpty()) {
                    adapter.setData(itemList)
                } else {
                    itemList.filter { item -> item.name.contains(searchText, ignoreCase = true) }
                        .let { filteredVillages ->
                            adapter.setData(filteredVillages)
                        }
                }
            }
        }
    }
}