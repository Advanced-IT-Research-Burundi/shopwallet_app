package com.shopwallet.shopwallet.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String,
    val description: String,
    val amount: Double,
    val date: String,
    val type: TransactionType,
    val status: TransactionStatus = TransactionStatus.COMPLETED
)

@Serializable
enum class TransactionType {
    @SerialName("PURCHASE") PURCHASE,
    @SerialName("TOPUP") TOPUP,
    @SerialName("REFUND") REFUND
}

@Serializable
enum class TransactionStatus {
    @SerialName("PENDING") PENDING,
    @SerialName("COMPLETED") COMPLETED,
    @SerialName("FAILED") FAILED
}
