package com.shopwallet.shopwallet.ui.navigation

import org.jetbrains.compose.resources.StringResource
import shopwallet.composeapp.generated.resources.Res
import shopwallet.composeapp.generated.resources.label_brand
import shopwallet.composeapp.generated.resources.label_cart
import shopwallet.composeapp.generated.resources.label_history
import shopwallet.composeapp.generated.resources.label_wallet


sealed class Screen(val route: String) {
  object Auth : Screen("auth")
  object Brand : Screen("brand")
  object Wallet : Screen("wallet")
  object Cart : Screen("cart")
  object History : Screen("history")
  object Main : Screen("main")
}

sealed class BottomNavScreen(val route: String, val labelRes: StringResource) {
  object Brand : BottomNavScreen(Screen.Brand.route, Res.string.label_brand)
  object Wallet : BottomNavScreen(Screen.Wallet.route, Res.string.label_wallet)
  object Cart : BottomNavScreen(Screen.Cart.route, Res.string. label_cart)
  object History: BottomNavScreen(Screen.History.route, Res.string.label_history)
}
