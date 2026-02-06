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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.shopwallet.shopwallet.data.brands
import com.shopwallet.shopwallet.data.model.Brand
import com.shopwallet.shopwallet.data.model.Product
import com.shopwallet.shopwallet.ui.navigation.BottomNavBar
import com.shopwallet.shopwallet.ui.navigation.BottomNavScreen
import com.shopwallet.shopwallet.ui.navigation.MainScaffold
import com.shopwallet.shopwallet.ui.theme.ShopWalletTheme
import com.shopwallet.shopwallet.ui.theme.toColor
import androidx.navigation.NavHostController
import com.shopwallet.shopwallet.ui.navigation.Screen
import com.shopwallet.shopwallet.ui.viewmodel.BrandViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.runtime.collectAsState

@Composable
fun BrandMainScreen(
    brand: Brand,
    currentRoute: String,
    navController: NavHostController,
    viewModel: BrandViewModel
) {
  val title = when {
      currentRoute.contains("/wallet") -> "My Wallet"
      currentRoute.contains("/history") -> "History"
      else -> brand.name
  }

  ShopWalletTheme(brandColor = brand.primaryColor.toColor()) {
    MainScaffold(
      title = title,
      bottomBar = {
        BottomNavBar(
          selectedRoute = currentRoute,
          onItemSelected = { screen ->
              navController.navigate(screen.route.replace("{brandId}", brand.id)) {
                  popUpTo(Screen.BrandDetails.createRoute(brand.id)) {
                      saveState = true
                  }
                  launchSingleTop = true
                  restoreState = true
              }
          }
        )
      }
    ) {
        when {
            currentRoute.contains("/wallet") -> {
                WalletScreen(
                    brand = brand,
                    viewModel = viewModel
                )
            }
            currentRoute.contains("/history") -> {
                HistoryScreen(viewModel = viewModel)
            }
            else -> {
                // Default to Wallet if route is unknown
                WalletScreen(
                    brand = brand,
                    viewModel = viewModel
                )
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