package com.mandi.repository

import com.mandi.model.DummyData
import com.mandi.model.SellMyProduceResponse
import com.mandi.model.Seller
import com.mandi.model.Village
import kotlinx.coroutines.delay

class SellerRepository {

    suspend fun getVillageList(): List<Village> {
        delay(1000) // Dummy delay
        return DummyData.villages
    }

    suspend fun getSellerList(): List<Seller> {
        delay(1000) // Dummy delay
        return DummyData.sellers
    }

    suspend fun completeSell(
        seller: String,
        price: String,
        weight: String
    ): SellMyProduceResponse {
        delay(1000) // Dummy delay
        return SellMyProduceResponse(seller, price, weight)
    }
}