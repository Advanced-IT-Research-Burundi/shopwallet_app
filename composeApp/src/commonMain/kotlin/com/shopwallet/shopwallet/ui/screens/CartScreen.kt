package com.shopwallet.shopwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
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

@Composable
fun CartScreen(cartItems: List<CartItem>) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    if (cartItems.isEmpty()) {
      EmptyCartView()
    } else {
      CartContent(cartItems)
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
fun CartContent(cartItems: List<CartItem>) {
  val totalPrice = cartItems.sumOf { it.product.price * it.quantity }

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
                text = "Order Summary",
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
                        text = "TOTAL PRICE",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "$${totalPrice.let { if (it.toString().contains(".")) it else "$it.00" }}",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        "Checkout",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Selected Items",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    // 2. Cart Items with Dividers
    itemsIndexed(cartItems) { index, item ->
      CartItemRow(item)
      if (index < cartItems.size - 1) {
        HorizontalDivider(
          modifier = Modifier.padding(vertical = 12.dp),
          thickness = 0.5.dp,
          color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
      }
    }
    
    // Bottom padding
    item {
        Spacer(modifier = Modifier.height(40.dp))
    }
  }
}

@Composable
fun CartItemRow(item: CartItem) {
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
          text = "$${item.product.price}",
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.primary
        )
      }
      
      // Quantity Controls
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
      ) {
        IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(28.dp)) {
          Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(14.dp))
        }
        Text(
          text = item.quantity.toString(),
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
          modifier = Modifier.padding(horizontal = 4.dp)
        )
        IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(28.dp)) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
        }
      }
    }
}