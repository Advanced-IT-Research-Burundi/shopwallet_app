package com.shopwallet.shopwallet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val bottomBar: @Composable () -> Unit = {
    BottomNavBar(
      selectedRoute = currentRoute,
      onItemSelected = { screen ->
        navController.navigate(screen.route) {
          popUpTo(Screen.Brand.route) {
            saveState = true
          }
          launchSingleTop = true
          restoreState = true
        }
      }
    )
  }

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
      MainScreen()
    }
    composable(Screen.Wallet.route) {
      MainScreen()
    }
    composable(Screen.Cart.route) {
      MainScreen()
    }
    composable(Screen.History.route) {
      MainScreen()
    }
  }
}
