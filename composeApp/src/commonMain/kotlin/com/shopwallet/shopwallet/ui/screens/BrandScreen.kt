package com.shopwallet.shopwallet.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.shopwallet.shopwallet.data.brands
import com.shopwallet.shopwallet.data.categories
import com.shopwallet.shopwallet.data.model.Brand
import com.shopwallet.shopwallet.data.products
import com.shopwallet.shopwallet.ui.components.ShopInput

@Composable
fun BrandScreen(brand: Brand) {
  var selectedCategory by remember { mutableStateOf("all") }
  var searchQuery by remember { mutableStateOf("") }

  // Use the passed brand
  val currentBrand = brand

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

    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      contentPadding = PaddingValues(bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {
      // 1. BRAND HEADER SECTION
      item(span = { GridItemSpan(2) }) {
        BrandHeader(currentBrand)
      }

        // 2. SEARCH & FILTER SECTION
        item(span = { GridItemSpan(2) }) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp)
          ) {
              // Search Bar
            ShopInput(
              value = searchQuery,
              onValueChange = { searchQuery = it },
              placeholder = "Search in ${currentBrand.name}...",
              leadingIcon = {
                Icon(
                  Icons.Default.Search,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              },
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Categories
            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              items(categories) { category ->
                CategoryChip(
                  name = category.name,
                  isSelected = selectedCategory == category.id,
                  onClick = { selectedCategory = category.id }
                )
              }
            }
          }
        }

        // 3. PRODUCT GRID
        if (filteredProducts.isEmpty()) {
          item(span = { GridItemSpan(2) }) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                "No products found",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        } else {
           items(filteredProducts) { product ->
             val index = filteredProducts.indexOf(product)
             val startPadding = if (index % 2 == 0) 16.dp else 8.dp // index even
             val endPadding = if (index % 2 == 0) 8.dp else 16.dp // index odd

             ProductCard(
              name = product.name,
              description = product.description,
              price = product.price,
              image = product.image,
              onAddToCart = { /* TODO */ },
              modifier = Modifier.padding(start = startPadding, end = endPadding)
             )
          }
        }
    }
}

@Composable
fun BrandHeader(brand: Brand) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(280.dp)
      .padding(bottom = 24.dp)
  ) {
    // Cover Image
    AsyncImage(
      model = brand.coverImage,
      contentDescription = "Brand Cover",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 40.dp)
        .background(MaterialTheme.colorScheme.surfaceVariant)
    )
      
      // Gradient Overlay
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = 40.dp)
          .background(
            Brush.verticalGradient(
              colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
              startY = 100f
            )
          )
      )

      // Brand Info on top of Cover
      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(start = 24.dp, bottom = 60.dp) // Push up from the bottom of the cover
      ) {
        Text(
          text = brand.name,
          style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
          color = Color.White
        )
        Text(
          text = brand.description,
          style = MaterialTheme.typography.bodyMedium,
          color = Color.White.copy(alpha = 0.9f),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.width(250.dp)
        )
      }

      // Logo
      Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(end = 24.dp)
          .size(80.dp)
          .offset(y = 0.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
           AsyncImage(
            model = brand.logo,
            contentDescription = "${brand.name} Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
          )
        }
      }
  }
}

@Composable
fun CategoryChip(
  name: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh
  val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(50))
      .background(backgroundColor)
      .clickable(onClick = onClick)
      .padding(horizontal = 20.dp, vertical = 10.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = name,
      color = contentColor,
      style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
    )
  }
}

@Composable
fun ProductCard(
    name: String,
    description: String,
    price: Double,
    image: String,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { /* TODO: Navigate to details */ },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column {
    // Image Container
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f) // Square aspect ratio
    ) {
      AsyncImage(
        model = image,
        contentDescription = name,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
      
      // Favorite Button
      IconButton(
        onClick = { /* TODO */ },
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(8.dp)
          .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), CircleShape)
          .size(32.dp)
      ) {
        Icon(
          Icons.Default.FavoriteBorder,
          contentDescription = "Favorite",
          modifier = Modifier.size(18.dp),
          tint = MaterialTheme.colorScheme.onSurface
        )
        }
    }

      // Content
      Column(modifier = Modifier.padding(12.dp)) {
        Text(
          text = name,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
          text = description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "$$price",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
          )
            
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable(onClick = onAddToCart)
                .padding(8.dp)
            ) {
              Icon(
                Icons.Default.Add,
                contentDescription = "Add to Cart",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
              )
            }
        }
      }
    }
  }
}