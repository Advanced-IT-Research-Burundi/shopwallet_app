package com.shopwallet.shopwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.shopwallet.shopwallet.data.brands
import com.shopwallet.shopwallet.data.model.Brand
import com.shopwallet.shopwallet.data.model.CartItem
import com.shopwallet.shopwallet.data.model.Product
import com.shopwallet.shopwallet.ui.navigation.BottomNavBar
import com.shopwallet.shopwallet.ui.navigation.BottomNavScreen
import com.shopwallet.shopwallet.ui.navigation.MainScaffold
import com.shopwallet.shopwallet.ui.theme.ShopWalletTheme
import com.shopwallet.shopwallet.ui.theme.toColor

@Composable
fun MainScreen() {
  var selectedTab by rememberSaveable(
    stateSaver = Saver(
      save = { it.route },
      restore = { route ->
        when (route) {
          BottomNavScreen.Brand.route -> BottomNavScreen.Brand
          BottomNavScreen.Wallet.route -> BottomNavScreen.Wallet
          BottomNavScreen.Cart.route -> BottomNavScreen.Cart
          BottomNavScreen.History.route -> BottomNavScreen.History
          else -> BottomNavScreen.Brand
        }
      }
    )
  ) { mutableStateOf<BottomNavScreen>(BottomNavScreen.Brand) }

  var selectedBrand by remember { mutableStateOf<Brand?>(null) }
  var cartItems by remember(selectedBrand?.id) { mutableStateOf(listOf<CartItem>()) }

  val addToCartValue: (Product) -> Unit = { product ->
    val existingItem = cartItems.find { it.product.id == product.id }
    if (existingItem != null) {
      cartItems = cartItems.map {
        if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
      }
    } else {
      cartItems = cartItems + CartItem(product)
    }
  }

  val isBrandSelected = selectedBrand != null

  val title = if (isBrandSelected) selectedBrand!!.name else "ShopWallet"

  Box(modifier = Modifier.fillMaxSize()) {
    MainScaffold(
      title = title,
      showBackButton = isBrandSelected,
      onBackClick = {
          if (selectedTab != BottomNavScreen.Brand) {
              selectedTab = BottomNavScreen.Brand
          } else {
              selectedBrand = null
          }
      },
      bottomBar = {
        if (isBrandSelected) {
             BottomNavBar(
              selectedRoute = selectedTab.route,
              onItemSelected = { selectedTab = it }
            )
        }
      }
    ) {
      if (!isBrandSelected) {
        BrandsGrid(onBrandClick = { selectedBrand = it })
      } else {
        ShopWalletTheme(brandColor = selectedBrand?.primaryColor?.toColor()) {
            when (selectedTab) {
                BottomNavScreen.Brand -> BrandScreen(brand = selectedBrand!!, onAddToCart = addToCartValue)
                BottomNavScreen.Wallet -> WalletScreen(brand = selectedBrand!!)
                BottomNavScreen.Cart -> CartScreen(cartItems = cartItems)
                BottomNavScreen.History -> HistoryScreen()
            }
        }
      }
    }
  }
}

@Composable
fun BrandsGrid(onBrandClick: (Brand) -> Unit) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
  ) {
    items(brands) { brand ->
      BrandGridCard(brand, onClick = { onBrandClick(brand) })
    }
  }
}

@Composable
fun BrandGridCard(brand: Brand, onClick: () -> Unit) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick),
      shape = RoundedCornerShape(16.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(0.8f) // Taller card for brand
      ) {
          // Background Image
        AsyncImage(
          model = brand.coverImage,
          contentDescription = brand.name,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
          
          // Gradient Overlay
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                  startY = 100f
                )
              )
          )

            // Content
            Column(
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
          ) {
                // Logo (Small)
            Card(
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.padding(bottom = 8.dp).size(40.dp)
            ) {
              AsyncImage(
                model = brand.logo,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().padding(4.dp)
              )
            }
              
              Text(
                text = brand.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = "${(10..100).random()} Products", // Mock count
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.8f)
              )
            }
        }
    }
}