package com.shopwallet.shopwallet.data.remote

import com.shopwallet.shopwallet.data.model.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class KtorClient(private val client: HttpClient, private val baseUrl: String) {

    // --- Auth ---
    suspend fun requestOtp(phone: String): ApiResponse<OtpResponse> =
        client.post("$baseUrl/auth/request-otp") {
            contentType(ContentType.Application.Json)
            setBody(OtpRequest(phone))
        }.body()

    suspend fun verifyOtp(phone: String, otp: String): ApiResponse<VerifyOtpResponse> =
        client.post("$baseUrl/auth/verify-otp") {
            contentType(ContentType.Application.Json)
            setBody(VerifyOtpRequest(phone, otp))
        }.body()

    suspend fun getUser(): ApiResponse<UserResponse> =
        client.get("$baseUrl/auth/user") {
            contentType(ContentType.Application.Json)
        }.body()

    suspend fun logout(token: String?): ApiResponse<EmptyResponse> =
        client.post("$baseUrl/auth/logout") {
            contentType(ContentType.Application.Json)
            if (!token.isNullOrEmpty()) {
                parameter("token", token)
            }
        }.body()

    // --- Brands ---
    suspend fun getBrand(id: String): ApiResponse<CompanyResponse> =
        client.get("$baseUrl/companies/$id") {
            contentType(ContentType.Application.Json)
        }.body()

    suspend fun getProducts(id: String): ApiResponse<List<Product>> =
        client.get("$baseUrl/companies/$id/products") {
            contentType(ContentType.Application.Json)
        }.body()

    // subscriptions
    suspend fun getSubscriptions(): ApiResponse<SubscriptionListResponse> =
        client.get("$baseUrl/subscriptions") {
            contentType(ContentType.Application.Json)
        }.body()

    suspend fun getSubscription(subscriptionId: Int): ApiResponse<SubscriptionResponse> =
        client.get("$baseUrl/subscriptions/$subscriptionId") {
            contentType(ContentType.Application.Json)
        }.body()

    // --- Wallet ---
    suspend fun getWallet(subscriptionId: Int): ApiResponse<WalletResponse> =
        client.get("$baseUrl/subscriptions/$subscriptionId/wallet") {
            contentType(ContentType.Application.Json)
        }.body()

    suspend fun getTransactions(subscriptionId: Int): ApiResponse<PaginatedResponse<Transaction>> =
        client.get("$baseUrl/subscriptions/$subscriptionId/wallet/transactions") {
            contentType(ContentType.Application.Json)
        }.body()
}
