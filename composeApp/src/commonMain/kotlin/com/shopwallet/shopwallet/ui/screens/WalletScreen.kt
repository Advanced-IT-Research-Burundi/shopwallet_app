package com.shopwallet.shopwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopwallet.shopwallet.data.model.Brand
import com.shopwallet.shopwallet.data.model.Transaction
import com.shopwallet.shopwallet.data.model.TransactionType
import com.shopwallet.shopwallet.ui.theme.LocalBrandColor

@Composable
fun WalletScreen(brand: Brand) {
    val brandColor = LocalBrandColor.current
    var isAmountVisible by remember { mutableStateOf(true) }
    
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
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Dashboard Header
        item {
            WalletDashboardHeader(
                brand = brand,
                balance = 124.50,
                brandColor = brandColor,
                isAmountVisible = isAmountVisible,
                onToggleVisibility = { isAmountVisible = !isAmountVisible }
            )
        }

        // 2. Activities Section
        item {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        itemsIndexed(transactions) { index, transaction ->
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                TransactionItem(transaction = transaction)
                if (index < transactions.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun WalletDashboardHeader(
    brand: Brand,
    balance: Double,
    brandColor: Color,
    isAmountVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Side: Balance Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TOTAL BALANCE",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAmountVisible) {
                            balance.toString().let { if (it.contains(".")) it else "$it.00" }
                        } else {
                            "••••••"
                        },
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 44.sp,
                            letterSpacing = if (isAmountVisible) (-1.5).sp else 2.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    IconButton(
                        onClick = onToggleVisibility,
                        modifier = Modifier.padding(start = 8.dp).size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isAmountVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Visibility",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tied Top Up Button
                Surface(
                    onClick = { /* TODO */ },
                    modifier = Modifier.height(44.dp).widthIn(min = 120.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = brandColor,
                    contentColor = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Top Up",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Right Side: Modern Wallet Icon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(brandColor.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                // Layered Card Icon Effect
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(brandColor, brandColor.copy(alpha = 0.6f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Professional Info Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = brandColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Funds protected for ${brand.name}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val (icon, tint) = when (transaction.type) {
        TransactionType.PURCHASE -> Icons.Default.ShoppingBag to MaterialTheme.colorScheme.primary
        TransactionType.TOPUP -> Icons.Default.AddCircle to Color(0xFF10B981)
        TransactionType.REFUND -> Icons.Default.Undo to Color(0xFF3B82F6)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
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
            text = "${if (transaction.amount > 0) "+" else "-"}$${kotlin.math.abs(transaction.amount).toString().let { if (it.contains(".")) it else "$it.00" }}",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = if (transaction.amount > 0) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface
        )
    }
}