package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.model.Transaction
import com.shopwallet.shopwallet.data.model.PaginatedResponse
import com.shopwallet.shopwallet.data.model.WalletResponse
import com.shopwallet.shopwallet.data.remote.ApiResponse
import com.shopwallet.shopwallet.data.remote.KtorClient
import com.shopwallet.shopwallet.utils.safeApiCall

interface WalletRepo {
    suspend fun getWallet(subscriptionId: Int): Result<ApiResponse<WalletResponse>>
    suspend fun getTransactions(subscriptionId: Int): Result<ApiResponse<PaginatedResponse<Transaction>>>
}

class WalletRepoImpl(
    private val client: KtorClient
) : WalletRepo {
    
    override suspend fun getWallet(subscriptionId: Int): Result<ApiResponse<WalletResponse>> =
        safeApiCall { client.getWallet(subscriptionId) }

    override suspend fun getTransactions(subscriptionId: Int): Result<ApiResponse<PaginatedResponse<Transaction>>> =
        safeApiCall { client.getTransactions(subscriptionId) }
}
