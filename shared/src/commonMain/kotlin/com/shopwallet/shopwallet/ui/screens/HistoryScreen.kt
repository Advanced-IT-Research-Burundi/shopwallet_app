package com.shopwallet.shopwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.runtime.collectAsState
import com.shopwallet.shopwallet.ui.viewmodel.BrandViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HistoryScreen(
    viewModel: BrandViewModel
) {
    var selectedType by remember { mutableStateOf<TransactionType?>(null) }
    val transactionsState by viewModel.transactionsState.collectAsState()
    
    val historyItems = transactionsState.data?.data ?: emptyList()

    val filteredItems = remember(selectedType, historyItems) {
        if (selectedType == null) historyItems else historyItems.filter { it.type == selectedType }
    }

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
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Type Filter Bar
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            label = "All",
                            isSelected = selectedType == null,
                            onClick = { selectedType = null }
                        )
                    }
                    // Filter out the old types for display if needed, but here we just use all entries
                    TransactionType.entries.filter { it == TransactionType.DEBIT || it == TransactionType.CREDIT }.forEach { type ->
                        item {
                            FilterChip(
                                label = type.name.lowercase().replaceFirstChar { it.uppercase() },
                                isSelected = selectedType == type,
                                onClick = { selectedType = type }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }

            if (transactionsState.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 64.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (filteredItems.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 64.dp), contentAlignment = Alignment.Center) {
                        Text(
                            "No transactions found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                itemsIndexed(filteredItems) { index, transaction ->
                    HistoryItemRow(transaction)
                    if (index < filteredItems.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HistoryItemRow(transaction: Transaction) {
    val (icon, tint) = when (transaction.type) {
        TransactionType.DEBIT -> Icons.Default.ShoppingBag to MaterialTheme.colorScheme.primary
        TransactionType.CREDIT -> Icons.Default.AddCircle to Color(0xFF10B981)
        else -> Icons.Default.HelpOutline to Color.Gray
    }

    val amount = transaction.amount.toDoubleOrNull() ?: 0.0

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
                text = if (transaction.description != "string") transaction.description else "Wallet Transaction",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = transaction.createdAt.take(16).replace("T", " "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (transaction.type == TransactionType.CREDIT) "+" else "-"}${CurrencyFormat.doubleToBif(amount)} BIF",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = if (transaction.type == TransactionType.CREDIT) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface
            )
            
            if (transaction.status != TransactionStatus.COMPLETED && transaction.status != TransactionStatus.COMPLETED_OLD) {
                Text(
                    text = transaction.status.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelSmall,
                    color = when(transaction.status) {
                        TransactionStatus.PENDING, TransactionStatus.PENDING_OLD -> Color(0xFFF59E0B)
                        TransactionStatus.FAILED, TransactionStatus.FAILED_OLD -> MaterialTheme.colorScheme.error
                        else -> Color.Unspecified
                    }
                )
            }
        }
    }
}