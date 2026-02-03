package com.shopwallet.shopwallet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shopwallet.shopwallet.ui.auth.AuthScreen
import com.shopwallet.shopwallet.ui.screens.BrandScreen
import com.shopwallet.shopwallet.ui.screens.CartScreen
import com.shopwallet.shopwallet.ui.screens.HistoryScreen
import com.shopwallet.shopwallet.ui.screens.MainScreen
import com.shopwallet.shopwallet.ui.screens.WalletScreen
import org.jetbrains.compose.resources.stringResource
import shopwallet.composeapp.generated.resources.Res
import shopwallet.composeapp.generated.resources.label_brand
import shopwallet.composeapp.generated.resources.label_cart
import shopwallet.composeapp.generated.resources.label_history
import shopwallet.composeapp.generated.resources.label_wallet

@Composable
fun AppNavigation(
  navController: NavHostController,
  onExit: () -> Unit
) {

  NavHost(
    navController = navController,
    startDestination = Screen.Auth.route
  ) {
    composable(Screen.Auth.route) {
      AuthScreen {
        navController.navigate(Screen.Brand.route) {
          popUpTo(Screen.Auth.route) { inclusive = true }
        }
      }
    }

    composable(Screen.Main.route) {
      MainScreen()
    }

    composable(Screen.Brand.route) {
      MainScaffold(
        title = stringResource(Res.string.label_brand),
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
      ) {
        BrandScreen()
      }
    }
    composable(Screen.Wallet.route) {
      MainScaffold(
        title = stringResource(Res.string.label_wallet),
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
      ) {
        WalletScreen()
      }
    }
    composable(Screen.Cart.route) {
      MainScaffold(
        title = stringResource(Res.string.label_cart),
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
      ) {
        CartScreen()
      }
    }
    composable(Screen.History.route) {
      MainScaffold(
        title = stringResource(Res.string.label_history),
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
      ) {
        HistoryScreen()
      }
    }
  }
}
