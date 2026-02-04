package com.shopwallet.shopwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.shopwallet.shopwallet.data.model.CartItem
import com.shopwallet.shopwallet.utils.CurrencyFormat

@Composable
fun CartScreen(
    cartItems: List<CartItem>, 
    walletBalance: Double,
    onRemoveItem: (String) -> Unit,
    onUpdateQuantity: (String, Int) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    if (cartItems.isEmpty()) {
      EmptyCartView()
    } else {
      CartContent(cartItems, walletBalance, onRemoveItem, onUpdateQuantity)
    }
  }
}

@Composable
fun EmptyCartView() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = Icons.Default.ShoppingCart,
      contentDescription = null,
      modifier = Modifier.size(80.dp),
      tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(
      text = "Your cart is empty",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = "Add some great products to continue",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    )
  }
}

@Composable
fun CartContent(
    cartItems: List<CartItem>, 
    walletBalance: Double,
    onRemoveItem: (String) -> Unit,
    onUpdateQuantity: (String, Int) -> Unit
) {
  val subtotal = cartItems.sumOf { it.product.price * it.quantity }
  val tax = subtotal * 0.10
  val total = subtotal + tax
  val debtMargin = 50000.0 // Allow up to 50,000 BIF debt
  
  val isInsufficient = total > walletBalance
  val isBeyondDebt = total > (walletBalance + debtMargin)
  val isInDebtMarge = isInsufficient && !isBeyondDebt

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(24.dp)
  ) {
    // 1. Checkout Summary at the Top (Unified but distinct)
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Checkout",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "ESTIMATED TOTAL",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "${CurrencyFormat.doubleToBif(total)} BIF",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp // Slightly smaller for larger numbers
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Button(
                    onClick = { /* TODO */ },
                    enabled = !isBeyondDebt,
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBeyondDebt) MaterialTheme.colorScheme.error.copy(alpha = 0.1f) else MaterialTheme.colorScheme.primary,
                        contentColor = if (isBeyondDebt) MaterialTheme.colorScheme.error else Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = if (isBeyondDebt) "Blocked" else if (isInDebtMarge) "Use Credit" else "Pay Now",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
            
            if (isInsufficient) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = if (isBeyondDebt) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f) else Color(0xFFFFF7ED),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isBeyondDebt) Icons.Default.Error else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (isBeyondDebt) MaterialTheme.colorScheme.error else Color(0xFFEA580C),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBeyondDebt) "Insufficient funds. Please top up." else "Credit needed. Amount due later.",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = if (isBeyondDebt) MaterialTheme.colorScheme.error else Color(0xFFEA580C)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Items",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    // 2. Cart Items with Dividers
    itemsIndexed(cartItems) { index, item ->
      CartItemRow(
          item = item,
          onRemove = { onRemoveItem(item.product.id) },
          onUpdateQuantity = { qty -> onUpdateQuantity(item.product.id, qty) }
      )
      if (index < cartItems.size - 1) {
        HorizontalDivider(
          modifier = Modifier.padding(vertical = 12.dp),
          thickness = 0.5.dp,
          color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
      }
    }
    
    // 3. Final Detailed Order Summary (Scrolling together)
    item {
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                .padding(24.dp)
        ) {
            Text(
                text = "Detailed Order Summary",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            SummaryRow("Subtotal", "${CurrencyFormat.doubleToBif(subtotal)} BIF")
            SummaryRow("Tax (10%)", "${CurrencyFormat.doubleToBif(tax)} BIF")
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            
            SummaryRow("Total", "${CurrencyFormat.doubleToBif(total)} BIF", isBold = true)
            SummaryRow("Wallet Balance", "${CurrencyFormat.doubleToBif(walletBalance)} BIF", color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            if (isInsufficient) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isBeyondDebt) "Insufficient funds. Please top up your wallet." else "Insufficient balance. Purchase will use credit margin.",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isBeyondDebt) MaterialTheme.colorScheme.error else Color(0xFFEA580C)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Final Checkout Button
            Button(
                onClick = { /* TODO */ },
                enabled = !isBeyondDebt,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isBeyondDebt) MaterialTheme.colorScheme.error.copy(alpha = 0.1f) else MaterialTheme.colorScheme.primary,
                    contentColor = if (isBeyondDebt) MaterialTheme.colorScheme.error else Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isBeyondDebt) {
                        Icon(Icons.Default.Error, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (isBeyondDebt) "Insufficient Funds" else if (isInDebtMarge) "Pay with Credit" else "Confirm Purchase",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
  }
}

@Composable
fun SummaryRow(label: String, value: String, isBold: Boolean = false, color: Color = Color.Unspecified) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
            color = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = if (isBold) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold) else MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = if (isBold) MaterialTheme.colorScheme.onSurface else if (color != Color.Unspecified) color else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onRemove: () -> Unit,
    onUpdateQuantity: (Int) -> Unit
) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Product Image
      AsyncImage(
        model = item.product.image,
        contentDescription = item.product.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .size(70.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      )
      
      Spacer(modifier = Modifier.width(16.dp))
      
      // Details
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.product.name,
          style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = item.product.category.replaceFirstChar { it.uppercase() },
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
          text = "${CurrencyFormat.doubleToBif(item.product.price)} BIF",
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.primary
        )
      }
      
      // Quantity Controls
      Column(horizontalAlignment = Alignment.End) {
          IconButton(
              onClick = onRemove,
              modifier = Modifier.size(32.dp)
          ) {
              Icon(
                  imageVector = Icons.Default.DeleteOutline,
                  contentDescription = "Remove",
                  tint = MaterialTheme.colorScheme.error,
                  modifier = Modifier.size(20.dp)
              )
          }
          
          Spacer(modifier = Modifier.height(8.dp))
          
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
          ) {
            IconButton(
                onClick = { onUpdateQuantity(item.quantity - 1) }, 
                modifier = Modifier.size(28.dp)
            ) {
              Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(14.dp))
            }
            Text(
              text = item.quantity.toString(),
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(horizontal = 4.dp)
            )
            IconButton(
                onClick = { onUpdateQuantity(item.quantity + 1) }, 
                modifier = Modifier.size(28.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
            }
          }
      }
    }
}