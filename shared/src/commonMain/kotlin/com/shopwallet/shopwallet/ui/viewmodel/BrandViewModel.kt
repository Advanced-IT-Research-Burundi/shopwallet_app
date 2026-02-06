package com.shopwallet.shopwallet.ui.viewmodel

import com.shopwallet.shopwallet.data.model.WalletResponse
import com.shopwallet.shopwallet.data.repository.WalletRepo
import com.shopwallet.shopwallet.utils.UiState
import com.shopwallet.shopwallet.utils.launchWithState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BrandViewModel(
    private val brandId: String,
    private val walletRepo: WalletRepo
) : androidx.lifecycle.ViewModel() {

    private val _walletState = MutableStateFlow(UiState<WalletResponse>())
    val walletState = _walletState.asStateFlow()

    fun loadWallet(subscriptionId: String) {
        launchWithState(
            stateFlow = _walletState,
            block = { walletRepo.getWallet(subscriptionId) },
            onSuccess = { wallet ->
                _walletState.value = UiState(data = wallet)
            },
            onFailure = { error ->
                _walletState.value = UiState(error = error.message ?: "Failed to load wallet")
            }
        )
    }
}

