package com.mandi.repository

import com.mandi.model.DummyData
import com.mandi.model.Seller
import com.mandi.model.Village

class SellerRepository {

    suspend fun getVillageList(): List<Village> {
        return DummyData.villages
    }

    suspend fun getSellerList(): List<Seller> {
        return DummyData.sellers
    }
}