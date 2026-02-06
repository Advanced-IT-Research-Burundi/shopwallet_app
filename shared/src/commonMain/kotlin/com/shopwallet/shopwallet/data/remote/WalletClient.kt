package com.shopwallet.shopwallet.data.remote

import com.shopwallet.shopwallet.data.model.WalletResponse
import com.shopwallet.shopwallet.data.remote.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WalletClient(private val client: HttpClient) {
    suspend fun getWallet(subscriptionId: String): ApiResponse<WalletResponse> =
        client.get("subscriptions/$subscriptionId/wallet").body()
}
