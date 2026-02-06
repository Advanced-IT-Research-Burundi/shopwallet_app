package com.shopwallet.shopwallet.data.model

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val date: String,
    val type: TransactionType,
    val status: TransactionStatus = TransactionStatus.COMPLETED
)

enum class TransactionType {
    PURCHASE, TOPUP, REFUND
}

enum class TransactionStatus {
    PENDING, COMPLETED, FAILED
}
