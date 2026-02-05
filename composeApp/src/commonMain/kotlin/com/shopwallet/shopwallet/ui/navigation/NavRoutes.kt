package com.shopwallet.shopwallet.ui.navigation

import org.jetbrains.compose.resources.StringResource
import shopwallet.composeapp.generated.resources.Res
import shopwallet.composeapp.generated.resources.label_brand
import shopwallet.composeapp.generated.resources.label_cart
import shopwallet.composeapp.generated.resources.label_history
import shopwallet.composeapp.generated.resources.label_wallet


sealed class Screen(val route: String) {
  object Auth : Screen("auth")
  object Brands : Screen("brands")
  object BrandDetails : Screen("brand/{brandId}") {
    fun createRoute(brandId: String) = "brand/$brandId"
  }
  object Wallet : Screen("brand/{brandId}/wallet") {
    fun createRoute(brandId: String) = "brand/$brandId/wallet"
  }
  object Cart : Screen("brand/{brandId}/cart") {
    fun createRoute(brandId: String) = "brand/$brandId/cart"
  }
  object History : Screen("brand/{brandId}/history") {
    fun createRoute(brandId: String) = "brand/$brandId/history"
  }
  object ProductDetails : Screen("brand/{brandId}/product/{productId}") {
    fun createRoute(brandId: String, productId: String) = "brand/$brandId/product/$productId"
  }
  object TopUp : Screen("brand/{brandId}/topup") {
    fun createRoute(brandId: String) = "brand/$brandId/topup"
  }
  object Checkout : Screen("brand/{brandId}/checkout") {
    fun createRoute(brandId: String) = "brand/$brandId/checkout"
  }
}

sealed class BottomNavScreen(val route: String, val labelRes: StringResource) {
  companion object {
    fun fromRoute(route: String?): BottomNavScreen? {
      return when {
        route?.contains("/wallet") == true -> Wallet
        route?.contains("/cart") == true -> Cart
        route?.contains("/history") == true -> History
        route?.contains("brand/") == true && !route.contains("/") -> Brand // This logic might need refinement
        else -> null
      }
    }
  }
  object Brand : BottomNavScreen("brand/{brandId}", Res.string.label_brand)
  object Wallet : BottomNavScreen("brand/{brandId}/wallet", Res.string.label_wallet)
  object Cart : BottomNavScreen("brand/{brandId}/cart", Res.string.label_cart)
  object History: BottomNavScreen("brand/{brandId}/history", Res.string.label_history)
}
