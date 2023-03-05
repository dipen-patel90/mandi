package com.mandi.common


sealed class BottomSheetConfig {
    // When content is small and it will fit at the bottom of the screen
    class Fixed(val isCancelable: Boolean = false) : BottomSheetConfig()

    // When full screen mode is required with little bit transparent space at the top
    class FullTransparentTop(val isCancelable: Boolean = false, val transparentTopPercentage: Int) :
        BottomSheetConfig()

    // When full screen dialog is required
    class Full(val isCancelable: Boolean = false) : BottomSheetConfig()

    // When there is edit box in the layout and we want to bring whole view on the top of the keyboard when keyboard is open
    class Edit(val isCancelable: Boolean = false) : BottomSheetConfig()

    // When full screen dialog is required and non draggable
    class FullNonDraggable(val isCancelable: Boolean = false) : BottomSheetConfig()

    // When full screen dialog is required and non draggable
    class ExpandedNonDraggable(val isCancelable: Boolean = false) : BottomSheetConfig()
}
