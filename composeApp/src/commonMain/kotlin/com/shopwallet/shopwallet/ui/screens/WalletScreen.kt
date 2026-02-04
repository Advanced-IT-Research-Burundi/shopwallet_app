package com.shopwallet.shopwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopwallet.shopwallet.data.model.Brand
import com.shopwallet.shopwallet.data.model.Transaction
import com.shopwallet.shopwallet.data.model.TransactionStatus
import com.shopwallet.shopwallet.data.model.TransactionType
import com.shopwallet.shopwallet.ui.theme.LocalBrandColor

@Composable
fun WalletScreen(brand: Brand) {
    val brandColor = LocalBrandColor.current
    
    // Mock data for transactions
    val transactions = listOf(
        Transaction("1", "Purchase at ${brand.name}", -45.00, "Today, 2:30 PM", TransactionType.PURCHASE),
        Transaction("2", "Wallet Top-up", 100.00, "Yesterday, 10:15 AM", TransactionType.TOPUP),
        Transaction("3", "Refund - Order #1234", 25.50, "Feb 2, 2024", TransactionType.REFUND),
        Transaction("4", "Purchase at ${brand.name}", -12.99, "Feb 1, 2024", TransactionType.PURCHASE),
        Transaction("5", "Wallet Top-up", 50.00, "Jan 28, 2024", TransactionType.TOPUP)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Balance Card
        item {
            BalanceCard(brand = brand, balance = 124.50, brandColor = brandColor)
        }

        // 2. Quick Actions
        item {
            QuickActions(brandColor = brandColor)
        }

        // 3. Transactions Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(onClick = { /* TODO */ }) {
                    Text("See All", color = brandColor)
                }
            }
        }

        // 4. Transactions List
        items(transactions) { transaction ->
            TransactionItem(transaction = transaction)
        }
    }
}

@Composable
fun BalanceCard(brand: Brand, balance: Double, brandColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(brandColor, brandColor.copy(alpha = 0.7f))
                    )
                )
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${brand.name} Wallet",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "Active Balance",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "$${balance}",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "**** **** **** 4257",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 2.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActions(brandColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionItem(icon = Icons.Default.Add, label = "Add Funds", color = brandColor)
        ActionItem(icon = Icons.Default.Payment, label = "Pay", color = brandColor)
        ActionItem(icon = Icons.Default.Send, label = "Send", color = brandColor)
        ActionItem(icon = Icons.Default.ReceiptLong, label = "Vouchers", color = brandColor)
    }
}

@Composable
fun ActionItem(icon: ImageVector, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { /* TODO */ }.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val (icon, tint) = when (transaction.type) {
        TransactionType.PURCHASE -> Icons.Default.ShoppingBag to Color(0xFFEF4444)
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
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
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
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = transaction.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "${if (transaction.amount > 0) "+" else ""}$${transaction.amount}",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = if (transaction.amount > 0) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface
        )
    }
}