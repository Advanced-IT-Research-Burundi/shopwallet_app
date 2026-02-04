package com.shopwallet.shopwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.shopwallet.shopwallet.data.brands
import com.shopwallet.shopwallet.data.categories
import com.shopwallet.shopwallet.data.products
import com.shopwallet.shopwallet.ui.components.ShopButton
import com.shopwallet.shopwallet.ui.components.ShopInput

@Composable
fun BrandScreen() {
  var selectedCategory by remember { mutableStateOf("all") }
  var searchQuery by remember { mutableStateOf("") }

  // Simulate "current brand" as the first one for now
  val currentBrand = brands.first()

  val brandProducts = remember(currentBrand.id) {
    products.filter { it.brandId == currentBrand.id }
  }

  val filteredProducts by remember(selectedCategory, searchQuery) {
    derivedStateOf {
      brandProducts.filter { product ->
        val matchesCategory = selectedCategory == "all" || product.category == selectedCategory
        val matchesSearch = product.name.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
      }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Search Bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(16.dp)
    ) {
      ShopInput(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = "Search products...",
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      )
    }
    
    // Category Pills
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(categories) { category ->
        val isSelected = selectedCategory == category.id
        val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        val borderColor = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline

        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
              width = if (isSelected) 0.dp else 1.dp,
              color = borderColor,
              shape = CircleShape
            )
            .clickable { selectedCategory = category.id }
            .padding(horizontal = 16.dp, vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = category.name,
            color = contentColor,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
          )
        }
      }
    }

    // Product Grid
    if (filteredProducts.isEmpty()) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No products found", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    } else {
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(filteredProducts) { product ->
          ProductCard(
            name = product.name,
            description = product.description,
            price = product.price,
            image = product.image,
            onAddToCart = { /* TODO */ }
          )
        }
      }
    }
  }
}

@Composable
fun ProductCard(
  name: String,
  description: String,
  price: Double,
  image: String,
  onAddToCart: () -> Unit
) {
  Column(
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surface)
      .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
  ) {
    // Image Placeholder
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(0.75f)
        .background(MaterialTheme.colorScheme.surfaceVariant),
      contentAlignment = Alignment.Center
    ) {
      AsyncImage(
        model = image,
        contentDescription = name,
        modifier = Modifier.fillMaxSize()
      )
    }

    Column(modifier = Modifier.padding(12.dp)) {
      Text(
        text = name,
        style = MaterialTheme.typography.titleSmall,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 4.dp)
      )
      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 12.dp)
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "$${price}",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        
        ShopButton(
            onClick = onAddToCart,
            modifier = Modifier.height(32.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
             Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}