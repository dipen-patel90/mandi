package com.mandi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandi.common.UIField
import com.mandi.extention.empty
import com.mandi.model.SellMyProduceResponse
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

    private val _completeSellResponse = MutableSharedFlow<SellMyProduceResponse>()
    val completeSellResponse = _completeSellResponse.asSharedFlow()
    private fun completeSell() = viewModelScope.launch {
        _completeSellResponse.emit(
            sellerRepository.completeSell(
                uiSellerName.value.state.value,
                uiGrossPrice.value,
                uiWeight.value.state.value
            )
        )
    }
    // endregion - API Calls

    // region - Form fields
    val uiSellerName = MutableStateFlow(UIField())
    val uiLoyaltyCard = MutableStateFlow(UIField())
    val uiVillage = MutableStateFlow(UIField())
    val uiWeight = MutableStateFlow(UIField())

    val uiGrossPrice: MutableStateFlow<String> = MutableStateFlow(String.empty())
    val uiLoyaltyIndex: MutableStateFlow<String> = MutableStateFlow(String.empty())

    private var selectedSeller: Seller? = null
    private var selectedVillage: Village? = null

    fun setSelectedSeller(seller: Seller) {
        selectedSeller = seller
        uiSellerName.value.state.update { seller.toString() }
        uiLoyaltyCard.value.state.update { seller.cardId ?: String.empty() }
    }

    fun setOrClearSeller(sellerName: String) {
        val seller =
            _sellerList.value.firstOrNull { it.toString().equals(sellerName, ignoreCase = true) }
        seller?.let {
            setSelectedSeller(seller)
        } ?: run {
            selectedSeller = null
            uiLoyaltyCard.value.state.update { String.empty() }
        }
    }

    fun setSellerName(cardNumber: String) {
        _sellerList.value.firstOrNull { it.cardId.equals(cardNumber, ignoreCase = true) }?.let {
            setSelectedSeller(it)
        }
    }

    fun setSelectedVillage(selected: Village) {
        selectedVillage = selected
        uiVillage.value.state.update { selected.name }
    }


    private var submitButtonClick = false
    fun validateAndSubmit() {
        submitButtonClick = true

        if (validate(
                uiSellerName.value.state.value,
                uiLoyaltyCard.value.state.value,
                uiVillage.value.state.value,
                uiWeight.value.state.value
            )
        ) {
            completeSell()
        }
    }
    // endregion


    val calculate: Flow<Any> = combine(
        uiLoyaltyCard.value.state,
        uiVillage.value.state,
        uiWeight.value.state,
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

    val validate: Flow<Any> = combine(
        uiSellerName.value.state,
        uiLoyaltyCard.value.state,
        uiVillage.value.state,
        uiWeight.value.state,
    ) { name, card, village, weight ->
        // Start validating only when user has press submit button once
        if (submitButtonClick) {
            validate(name, card, village, weight)
        }
    }

    private fun calculateGrossValue(
        grossWeight: Int,
        villagePrice: Double,
        loyaltyIndex: Double
    ): Double {
        return grossWeight * villagePrice * loyaltyIndex
    }


    private fun validate(name: String, card: String, village: String, weight: String): Boolean {

        val sellerNameError = name.isEmpty()
        uiSellerName.update {
            uiSellerName.value.copy(
                hasError = sellerNameError,
                errorMessage = "Please enter seller name"
            )
        }

        val cardNumber = card
        val loyaltyError =
            cardNumber.isNotEmpty() && _sellerList.value.firstOrNull {
                it.cardId.equals(
                    cardNumber,
                    ignoreCase = true
                )
            } == null
        uiLoyaltyCard.update {
            uiLoyaltyCard.value.copy(
                hasError = loyaltyError,
                errorMessage = "Loyalty card number is invalid. Please enter correct card number."
            )
        }

        val villageError = village.isEmpty()
        uiVillage.update {
            uiVillage.value.copy(
                hasError = villageError,
                errorMessage = "Please select village"
            )
        }

        val weightError = weight.isEmpty()
        uiWeight.update {
            uiWeight.value.copy(
                hasError = weightError,
                errorMessage = "Please enter gross weight"
            )
        }

        return uiSellerName.value.hasError.not()
                && uiLoyaltyCard.value.hasError.not()
                && uiVillage.value.hasError.not()
                && uiWeight.value.hasError.not()
    }

    init {
        getSellerList()
        getVillageList()
    }
}