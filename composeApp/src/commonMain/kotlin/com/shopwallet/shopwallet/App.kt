package com.shopwallet.shopwallet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shopwallet.shopwallet.ui.auth.AuthScreen

import com.shopwallet.shopwallet.ui.theme.ShopWalletTheme

@Composable
fun App() {
  ShopWalletTheme {
    val navController = rememberNavController()

    NavHost(
      navController = navController,
      startDestination = "auth",
      modifier = Modifier.fillMaxSize()
    ) {
      composable("auth") {
        AuthScreen(
          onAuthenticated = {
            navController.navigate("home") {
              popUpTo("auth") { inclusive = true }
            }
          }
        )
      }

      composable("home") {
        Text("Welcome to Shop Wallet Home (Grade 3+)")
      }
    }
  }
}