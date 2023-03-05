package com.mandi.model

import com.mandi.extention.empty

data class Seller(val name: String, val cardId: String? = null) {

    override fun toString(): String {
        val cardId = if (cardId != null) " [ $cardId ]" else String.empty()
        return name + cardId
    }
}
