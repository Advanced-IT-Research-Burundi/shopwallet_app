package com.shopwallet.shopwallet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shopwallet.shopwallet.ui.navigation.AppNavigation
import com.shopwallet.shopwallet.ui.theme.ShopWalletTheme

@Composable
fun App() {
  ShopWalletTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colorScheme.background
    ) {
      AppNavigation()
    }
  }
}