package com.shopwallet.shopwallet.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Auth Models
@Serializable
data class OtpRequest(
    val phone: String
)

@Serializable
data class OtpResponse(
    val message: String? = null,
    val otp: String? = null,
    val success: Boolean = false
)

@Serializable
data class VerifyOtpRequest(
    val phone: String,
    val otp: String
)

@Serializable
data class VerifyOtpResponse(
    val token: String,
    val user: UserResponse? = null
)

@Serializable
data class UserResponse(
    val id: String,
    val phone: String,
    val name: String? = null,
    val email: String? = null
)

// Company Models
@Serializable
data class CompanyResponse(
    val id: String,
    val name: String,
    val description: String? = null,
    val logo: String? = null,
    val subscriptionId: String? = null
)

@Serializable
data class CompanyListResponse(
    val companies: List<CompanyResponse>
)

@Serializable
data class SubscriptionListResponse(
    val subscriptions: List<Subscription>
)

@Serializable
data class SubscriptionResponse(
    val subscription: Subscription
)

// Wallet Models
@Serializable
data class WalletResponse(
    @SerialName("wallet_id") val walletId: Int,
    val balance: String,
    @SerialName("subscription_id") val subscriptionId: Int,
    val company: WalletCompany
)

@Serializable
data class WalletCompany(
    val id: Int,
    val name: String
)

// Transaction Models
@Serializable
data class PaginatedResponse<T>(
    @SerialName("current_page") val currentPage: Int,
    val data: List<T>,
    val total: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("last_page") val lastPage: Int
)

// Generic API Response
@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val success: Boolean = false
)
