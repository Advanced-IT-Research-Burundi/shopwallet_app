package com.shopwallet.shopwallet.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.shopwallet.shopwallet.ui.navigation.BottomNavBar
import com.shopwallet.shopwallet.ui.navigation.BottomNavScreen
import com.shopwallet.shopwallet.ui.navigation.MainScaffold
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

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

  Box(modifier = Modifier.fillMaxSize()) {
    MainScaffold(
      title = stringResource(selectedTab.labelRes),
      showBackButton = false,
      onBackClick = {},
      bottomBar = {
        BottomNavBar(
          selectedRoute = selectedTab.route,
          onItemSelected = { selectedTab = it }
        )
      }
    ) {
      when (selectedTab) {
        BottomNavScreen.Brand -> BrandScreen()
        BottomNavScreen.Wallet -> WalletScreen()
        BottomNavScreen.Cart -> CartScreen()
        BottomNavScreen.History -> HistoryScreen()
      }
    }
  }
}