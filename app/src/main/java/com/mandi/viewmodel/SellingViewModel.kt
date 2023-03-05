package com.mandi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandi.common.UIField
import com.mandi.extention.empty
import com.mandi.model.Seller
import com.mandi.model.Village
import com.mandi.repository.SellerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellingViewModel @Inject constructor(private val sellerRepository: SellerRepository) :
    ViewModel() {


    // region - API Calls
    private val _sellerList = MutableStateFlow<List<Seller>>(listOf())
    val sellerList = _sellerList.asStateFlow()

    private fun getSellerList() = viewModelScope.launch {
        _sellerList.emit(sellerRepository.getSellerList())
    }

    private val _villageList = MutableStateFlow<List<Village>>(listOf())
    val villageList = _villageList.asStateFlow()

    private fun getVillageList() = viewModelScope.launch {
        _villageList.emit(sellerRepository.getVillageList())
    }
    // endregion - API Calls

    // region - Form fields
    val uiSellerName = UIField()
    val uiLoyaltyCard = UIField()
    val uiVillage = UIField()
    val uiWeight = UIField()

    val uiGrossPrice: MutableStateFlow<String> = MutableStateFlow(String.empty())
    val uiLoyaltyIndex: MutableStateFlow<String> = MutableStateFlow(String.empty())

    private var selectedSeller: Seller? = null
    private var selectedVillage: Village? = null

    fun setSelectedSeller(seller: Seller) {
        selectedSeller = seller
        uiSellerName.state.update { seller.toString() }
        uiLoyaltyCard.state.update { seller.cardId ?: String.empty() }
    }

    fun setOrClearSeller(sellerName: String) {
        val seller =
            _sellerList.value.firstOrNull { it.toString().equals(sellerName, ignoreCase = true) }
        seller?.let {
            setSelectedSeller(seller)
        } ?: run {
            selectedSeller = null
            uiLoyaltyCard.state.update { String.empty() }
        }
    }

    fun setSellerName(cardNumber: String) {
        _sellerList.value.firstOrNull { it.cardId.equals(cardNumber, ignoreCase = true) }?.let {
            setSelectedSeller(it)
        }
    }

    fun setSelectedVillage(selected: Village) {
        selectedVillage = selected
        uiVillage.state.update { selected.name }
    }


    fun validateAndSubmit() {
//        val sellerNameError = uiSellerName.state.value.isEmpty()
//        uiSellerName.errorMessage = "Something went wrong"
//        uiSellerName.hasError.update { sellerNameError }
    }
    // endregion


    val calculate: Flow<Any> = combine(
        uiLoyaltyCard.state,
        uiVillage.state,
        uiWeight.state,
    ) { card, village, weight ->

        val appliedLoyaltyIndex = if (selectedSeller?.cardId?.isNotEmpty() == true) 1.12 else 0.98
        uiLoyaltyIndex.update { appliedLoyaltyIndex.toString() }

        if (village.isNotEmpty() && weight.isNotEmpty()) {
            selectedVillage?.let { selectedvillage ->
                val grossPrice =
                    calculateGrossValue(weight.toInt(), selectedvillage.price, appliedLoyaltyIndex)
                uiGrossPrice.update { String.format("%.2f", grossPrice) }
            }
        }
    }

    private fun calculateGrossValue(
        grossWeight: Int,
        villagePrice: Double,
        loyaltyIndex: Double
    ): Double {
        return grossWeight * villagePrice * loyaltyIndex
    }

    private fun validate(
        grossWeight: Int,
        villagePrice: Double,
        loyaltyIndex: Double
    ): Double {
        return grossWeight * villagePrice * loyaltyIndex
    }

    init {
        getSellerList()
        getVillageList()
    }
}