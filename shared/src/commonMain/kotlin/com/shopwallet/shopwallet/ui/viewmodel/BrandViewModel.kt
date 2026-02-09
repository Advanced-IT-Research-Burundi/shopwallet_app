package com.shopwallet.shopwallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.shopwallet.shopwallet.data.model.Subscription
import com.shopwallet.shopwallet.data.model.SubscriptionListResponse
import com.shopwallet.shopwallet.data.model.WalletResponse
import com.shopwallet.shopwallet.data.model.Transaction
import com.shopwallet.shopwallet.data.model.PaginatedResponse
import com.shopwallet.shopwallet.data.repository.BrandRepo
import com.shopwallet.shopwallet.data.repository.WalletRepo
import com.shopwallet.shopwallet.utils.Logger
import com.shopwallet.shopwallet.utils.UiState
import com.shopwallet.shopwallet.utils.launchWithState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BrandViewModel(
    private val brandId: String,
    private val walletRepo: WalletRepo,
    private val brandRepo: BrandRepo
) : ViewModel() {

    private val _subscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptions = _subscriptions.asStateFlow()

    private val _walletState = MutableStateFlow(UiState<WalletResponse>())
    val walletState = _walletState.asStateFlow()

    private val _transactionsState = MutableStateFlow(UiState<PaginatedResponse<Transaction>>())
    val transactionsState = _transactionsState.asStateFlow()

    fun loadWallet(subscriptionId: Int) {
        launchWithState(
            stateFlow = _walletState,
            block = { walletRepo.getWallet(subscriptionId) }
        )
    }

    fun getTransactions(subscriptionId: Int) {
        launchWithState(
            stateFlow = _transactionsState,
            block = { walletRepo.getTransactions(subscriptionId) }
        )
    }

    private val _subscriptionsState = MutableStateFlow(UiState<SubscriptionListResponse>())
    val subscriptionsState = _subscriptionsState.asStateFlow()

    fun getSubscriptions() {
        launchWithState(
            _subscriptionsState,
            block = { brandRepo.getSubscriptions() },
            onSuccess = {
                _subscriptions.value = it?.subscriptions ?: emptyList()
            },
            onFailure = { e->
                e.message?.let { Logger.e("SUB", it)}
            }
        )
    }
}
