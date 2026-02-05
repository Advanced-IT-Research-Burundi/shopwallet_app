package com.shopwallet.shopwallet.ui.navigation
import com.shopwallet.shopwallet.data.brands

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shopwallet.shopwallet.ui.viewmodel.BrandViewModel
import com.shopwallet.shopwallet.ui.viewmodel.AuthViewModel
import com.shopwallet.shopwallet.ui.auth.AuthScreen
import com.shopwallet.shopwallet.ui.screens.BrandMainScreen
import com.shopwallet.shopwallet.ui.screens.BrandsGrid

import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavigation(
  navController: NavHostController
) {
  val authViewModel = koinViewModel<AuthViewModel>()

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  // Determine start destination
  val startDestination = if (authViewModel.isLoggedIn()) {
      Screen.Brands.route
  } else {
      Screen.Auth.route
  }

  NavHost(
    navController = navController,
    startDestination = startDestination
  ) {
    composable(Screen.Auth.route) {
      AuthScreen {
        // Auth handled in AuthViewModel now, but for flow:
        navController.navigate(Screen.Brands.route) {
          popUpTo(Screen.Auth.route) { inclusive = true }
        }
      }
    }

    composable(Screen.Brands.route) {
      MainScaffold(
        title = "ShopWallet",
        showBackButton = false,
        onBackClick = {}
      ) {
        BrandsGrid(onBrandClick = { brand ->
          navController.navigate(Screen.BrandDetails.createRoute(brand.id))
        })
      }
    }

    // Nested Brand Graph to share ViewModel/State
    navigation(
        startDestination = Screen.BrandDetails.route,
        route = "brand_graph/{brandId}"
    ) {
        composable(Screen.BrandDetails.route) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            BrandContainer(brandId, navController, Screen.BrandDetails.route)
        }
        composable(Screen.Wallet.route) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            BrandContainer(brandId, navController, Screen.Wallet.route)
        }
        composable(Screen.Cart.route) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            BrandContainer(brandId, navController, Screen.Cart.route)
        }
        composable(Screen.History.route) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            BrandContainer(brandId, navController, Screen.History.route)
        }
        composable(Screen.ProductDetails.route) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            val productId = backStackEntry.arguments?.getString("productId")
            BrandContainer(brandId, navController, Screen.ProductDetails.route, productId)
        }
        composable(Screen.TopUp.route) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            BrandContainer(brandId, navController, Screen.TopUp.route)
        }
        composable(Screen.Checkout.route) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            BrandContainer(brandId, navController, Screen.Checkout.route)
        }
    }
  }
}

@Composable
fun BrandContainer(
    brandId: String?,
    navController: NavHostController,
    currentRoute: String,
    productId: String? = null
) {
    val brand = brands.find { it.id == brandId } ?: return
    
    // Get the ViewModel scoped to this brandId
    val viewModel = koinViewModel<BrandViewModel>(key = brandId) { parametersOf(brandId) }
    
    BrandMainScreen(
        brand = brand,
        currentRoute = currentRoute,
        productId = productId,
        navController = navController,
        viewModel = viewModel
    )
}
