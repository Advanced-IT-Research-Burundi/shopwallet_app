package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.model.WalletResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

interface WalletRepo {
    suspend fun getWallet(subscriptionId: String): Result<WalletResponse>
}

class WalletRepoImpl(
    private val client: HttpClient
) : WalletRepo {
    
    override suspend fun getWallet(subscriptionId: String): Result<WalletResponse> {
        return try {
            val response = client.get("subscriptions/$subscriptionId/wallet").body<WalletResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
