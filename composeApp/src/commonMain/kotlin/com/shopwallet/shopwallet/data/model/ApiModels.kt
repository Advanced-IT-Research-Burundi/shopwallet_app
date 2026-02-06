package com.shopwallet.shopwallet.data.model

import kotlinx.serialization.Serializable

// Auth Models
@Serializable
data class OtpRequest(
    val phone: String
)

@Serializable
data class OtpResponse(
    val message: String? = null,
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
    val subscriptionId: String? = null // Assuming subscription ID is here
)

@Serializable
data class CompanyListResponse(
    val companies: List<CompanyResponse>
)

// Wallet Models
@Serializable
data class WalletResponse(
    val balance: Double,
    val currency: String = "BIF",
    val transactions: List<TransactionResponse> = emptyList()
)
 
@Serializable
data class TransactionResponse(
    val id: String,
    val description: String,
    val amount: Double,
    val date: String,
    val type: String // "PURCHASE", "TOPUP", "REFUND"
)

// Generic API Response
@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val success: Boolean = false
)
