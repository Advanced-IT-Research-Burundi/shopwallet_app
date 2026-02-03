package com.shopwallet.shopwallet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.shopwallet.shopwallet.ui.navigation.AppNavigation
import com.shopwallet.shopwallet.ui.theme.ShopWalletTheme

@Composable
fun App() {
  val navController = rememberNavController()

  ShopWalletTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colorScheme.background
    ) {
      AppNavigation(navController, onExit = {})
    }
  }
}