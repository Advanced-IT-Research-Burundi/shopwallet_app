package com.shopwallet.shopwallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopwallet.shopwallet.data.model.WalletResponse
import com.shopwallet.shopwallet.data.repository.WalletRepo
import com.shopwallet.shopwallet.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BrandViewModel(
    private val brandId: String,
    private val walletRepo: WalletRepo
) : androidx.lifecycle.ViewModel() {

    private val _walletState = MutableStateFlow(UiState<WalletResponse>())
    val walletState = _walletState.asStateFlow()

    fun loadWallet(subscriptionId: String) {
        viewModelScope.launch {
            _walletState.value = UiState(isLoading = true)
            walletRepo.getWallet(subscriptionId).fold(
                onSuccess = { wallet ->
                    _walletState.value = UiState(data = wallet)
                },
                onFailure = { error ->
                    _walletState.value = UiState(error = error.message ?: "Failed to load wallet")
                }
            )
        }
    }
}
