package com.shopwallet.shopwallet.ui.navigation

import org.jetbrains.compose.resources.StringResource
import shopwallet.shared.generated.resources.Res
import shopwallet.shared.generated.resources.label_brand
import shopwallet.shared.generated.resources.label_history
import shopwallet.shared.generated.resources.label_wallet


sealed class Screen(val route: String) {
  object Auth : Screen("auth")
  object Brands : Screen("brands")
  object BrandDetails : Screen("brand/{brandId}") {
    fun createRoute(brandId: String) = "brand/$brandId"
  }
  object Wallet : Screen("brand/{brandId}/wallet") {
    fun createRoute(brandId: String) = "brand/$brandId/wallet"
  }
  object History : Screen("brand/{brandId}/history") {
    fun createRoute(brandId: String) = "brand/$brandId/history"
  }
}

sealed class BottomNavScreen(val route: String, val labelRes: StringResource) {
  object Brand : BottomNavScreen("brand/{brandId}", Res.string.label_brand)
  object Wallet : BottomNavScreen("brand/{brandId}/wallet", Res.string.label_wallet)
  object History: BottomNavScreen("brand/{brandId}/history", Res.string.label_history)
}
