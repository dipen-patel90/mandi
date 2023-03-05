package com.mandi.view.base

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mandi.R
import com.mandi.common.BottomSheetConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseBottomSheet(private val bottomSheetDialogType: BottomSheetConfig) :
    BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cancelable = when (bottomSheetDialogType) {
            is BottomSheetConfig.Edit -> bottomSheetDialogType.isCancelable
            is BottomSheetConfig.Fixed -> bottomSheetDialogType.isCancelable
            is BottomSheetConfig.Full -> bottomSheetDialogType.isCancelable
            is BottomSheetConfig.FullTransparentTop -> bottomSheetDialogType.isCancelable
            is BottomSheetConfig.FullNonDraggable -> bottomSheetDialogType.isCancelable
            is BottomSheetConfig.ExpandedNonDraggable -> bottomSheetDialogType.isCancelable
        }
        if (cancelable) {
            setStyle(STYLE_NORMAL, R.style.BottomSheetDialog_Cancelable)
        } else {
            setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
        }
        isCancelable = cancelable
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        when (bottomSheetDialogType) {
            is BottomSheetConfig.Edit -> {
                dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
            is BottomSheetConfig.Fixed -> {}
            is BottomSheetConfig.Full -> {
                dialog.setOnShowListener { dialogInterface ->
                    val bottomSheetDialog = dialogInterface as BottomSheetDialog
                    setupFullHeight(bottomSheetDialog, 0)
                }
            }
            is BottomSheetConfig.FullTransparentTop -> {
                dialog.setOnShowListener { dialogInterface ->
                    val bottomSheetDialog = dialogInterface as BottomSheetDialog
                    setupFullHeight(
                        bottomSheetDialog,
                        bottomSheetDialogType.transparentTopPercentage
                    )
                }
            }
            is BottomSheetConfig.FullNonDraggable -> {
                dialog.setOnShowListener { dialogInterface ->
                    val bottomSheetDialog = dialogInterface as BottomSheetDialog
                    setupFullHeightNonDraggable(
                        bottomSheetDialog,
                        0
                    )
                }
            }
            is BottomSheetConfig.ExpandedNonDraggable -> {
                dialog.setOnShowListener { dialogInterface ->
                    val bottomSheetDialog = dialogInterface as BottomSheetDialog
                    setupFullExpandedNonDraggable(
                        bottomSheetDialog
                    )
                }
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog, transparentPercentage: Int) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        bottomSheet?.let {
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(it)
            val layoutParams = it.layoutParams
            if (layoutParams != null) {
                val height = Resources.getSystem().displayMetrics.heightPixels
                layoutParams.height = height - height * transparentPercentage / 100
            }
            it.layoutParams = layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setupFullHeightNonDraggable(
        bottomSheetDialog: BottomSheetDialog,
        transparentPercentage: Int
    ) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        bottomSheet?.let {
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(it)
            val layoutParams = it.layoutParams
            if (layoutParams != null) {
                val height = Resources.getSystem().displayMetrics.heightPixels
                layoutParams.height = height - height * transparentPercentage / 100
            }
            it.layoutParams = layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false
        }
    }

    private fun setupFullExpandedNonDraggable(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        bottomSheet?.let {
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false
        }
    }

    val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}
