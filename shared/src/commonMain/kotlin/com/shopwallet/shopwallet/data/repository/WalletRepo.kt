package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.model.WalletResponse
import com.shopwallet.shopwallet.data.remote.ApiResponse
import com.shopwallet.shopwallet.data.remote.KtorClient
import com.shopwallet.shopwallet.utils.safeApiCall

interface WalletRepo {
    suspend fun getWallet(subscriptionId: String): Result<ApiResponse<WalletResponse>>
}

class WalletRepoImpl(
    private val client: KtorClient
) : WalletRepo {
    
    override suspend fun getWallet(subscriptionId: String): Result<ApiResponse<WalletResponse>> =
        safeApiCall { client.getWallet(subscriptionId) }
}
