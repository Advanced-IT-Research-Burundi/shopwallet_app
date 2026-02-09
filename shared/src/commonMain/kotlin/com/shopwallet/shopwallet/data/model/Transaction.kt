package com.shopwallet.shopwallet.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: Int,
    @SerialName("wallet_id") val walletId: Int,
    @SerialName("company_id") val companyId: Int,
    val type: TransactionType,
    val amount: String,
    val reference: String,
    val status: TransactionStatus,
    val description: String,
    @SerialName("source_system") val sourceSystem: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    val company: TransactionCompany
)

@Serializable
data class TransactionCompany(
    val id: Int,
    val name: String
)

@Serializable
enum class TransactionType {
    @SerialName("debit") DEBIT,
    @SerialName("credit") CREDIT,
    @SerialName("PURCHASE") PURCHASE,
    @SerialName("TOPUP") TOPUP,
    @SerialName("REFUND") REFUND
}

@Serializable
enum class TransactionStatus {
    @SerialName("pending") PENDING,
    @SerialName("completed") COMPLETED,
    @SerialName("failed") FAILED,
    @SerialName("PENDING") PENDING_OLD,
    @SerialName("COMPLETED") COMPLETED_OLD,
    @SerialName("FAILED") FAILED_OLD
}
