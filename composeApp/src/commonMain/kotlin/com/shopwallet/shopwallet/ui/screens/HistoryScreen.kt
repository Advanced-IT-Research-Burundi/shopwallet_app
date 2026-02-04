package com.shopwallet.shopwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopwallet.shopwallet.data.model.Transaction
import com.shopwallet.shopwallet.data.model.TransactionStatus
import com.shopwallet.shopwallet.data.model.TransactionType
import com.shopwallet.shopwallet.utils.CurrencyFormat

@Composable
fun HistoryScreen() {
    val historyItems = listOf(
        Transaction("1", "Purchase at Nike", -25000.00, "Today, 10:45 AM", TransactionType.PURCHASE),
        Transaction("2", "Wallet Top-up", 150000.00, "Yesterday, 3:20 PM", TransactionType.TOPUP),
        Transaction("3", "Purchase at Tech Haven", -120000.00, "Feb 3, 2024", TransactionType.PURCHASE),
        Transaction("4", "Refund - Shoes #992", 45000.00, "Feb 2, 2024", TransactionType.REFUND),
        Transaction("5", "Purchase at Nike", -65000.00, "Feb 1, 2024", TransactionType.PURCHASE),
        Transaction("6", "Wallet Top-up", 200000.00, "Jan 30, 2024", TransactionType.TOPUP),
        Transaction("7", "Purchase at Local Store", -15000.00, "Jan 29, 2024", TransactionType.PURCHASE),
        Transaction("8", "Refund - Gadget #101", 12500.00, "Jan 28, 2024", TransactionType.REFUND)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp)
        ) {
            item {
                Text(
                    text = "Transaction History",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            itemsIndexed(historyItems) { index, transaction ->
                HistoryItemRow(transaction)
                if (index < historyItems.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun HistoryItemRow(transaction: Transaction) {
    val (icon, tint) = when (transaction.type) {
        TransactionType.PURCHASE -> Icons.Default.ShoppingBag to MaterialTheme.colorScheme.primary
        TransactionType.TOPUP -> Icons.Default.AddCircle to Color(0xFF10B981)
        TransactionType.REFUND -> Icons.Default.Undo to Color(0xFF3B82F6)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = transaction.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (transaction.amount > 0) "+" else "-"}${CurrencyFormat.doubleToBif(kotlin.math.abs(transaction.amount))} BIF",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = if (transaction.amount > 0) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface
            )
            
            if (transaction.status != TransactionStatus.COMPLETED) {
                Text(
                    text = transaction.status.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelSmall,
                    color = when(transaction.status) {
                        TransactionStatus.PENDING -> Color(0xFFF59E0B)
                        TransactionStatus.FAILED -> MaterialTheme.colorScheme.error
                        else -> Color.Unspecified
                    }
                )
            }
        }
    }
}