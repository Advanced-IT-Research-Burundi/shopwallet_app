package com.shopwallet.shopwallet.ui.navigation

import com.shopwallet.shopwallet.data.model.Brand
import com.shopwallet.shopwallet.data.model.Subscription

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.shopwallet.shopwallet.ui.viewmodel.BrandViewModel
import com.shopwallet.shopwallet.ui.viewmodel.AuthViewModel
import com.shopwallet.shopwallet.ui.auth.AuthScreen
import com.shopwallet.shopwallet.ui.auth.OtpScreen
import com.shopwallet.shopwallet.ui.screens.BrandMainScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.shopwallet.shopwallet.ui.screens.BrandsGrid
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AppNavigation(
  navController: NavHostController
) {
  val authViewModel = koinViewModel<AuthViewModel>()
  val logoutState by authViewModel.logoutState.collectAsState()

  LaunchedEffect(logoutState.data) {
    if (logoutState.data != null) {
      navController.navigate(Screen.Auth.route) {
        popUpTo(0) { inclusive = true }
      }
    }
  }

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
      AuthScreen(onOtpRequested = { phone ->
        navController.navigate(Screen.Otp.createRoute(phone))
      })
    }
    
    composable(Screen.Otp.route) { backStackEntry ->
      val phone = backStackEntry.arguments?.getString("phone") ?: ""
      OtpScreen(
        phone = phone,
        onAuthenticated = {
          navController.navigate(Screen.Brands.route) {
            popUpTo(Screen.Auth.route) { inclusive = true }
          }
        },
        onBack = {
          navController.popBackStack()
        }
      )
    }

    composable(Screen.Brands.route) {
      val viewModel = koinViewModel<BrandViewModel>(key = "global") { parametersOf("") }
      val subscriptions by viewModel.subscriptionsState.collectAsState()

      LaunchedEffect(Unit) {
          viewModel.getSubscriptions()
      }

      MainScaffold(
        title = "ShopWallet",
        onLogout = { authViewModel.logout() },
        bottomBar = {
            BottomNavBar(
                selectedRoute = Screen.Brands.route,
                onItemSelected = { screen ->
                    if (screen == BottomNavScreen.Brand) {
                        navController.navigate(Screen.Brands.route) {
                            popUpTo(Screen.Brands.route) { inclusive = true }
                        }
                    } else {
                        val defaultBrandId = viewModel.subscriptions.value.firstOrNull()?.company?.id?.toString() ?: ""
                        if (defaultBrandId.isNotEmpty()) {
                            navController.navigate(screen.route.replace("{brandId}", defaultBrandId))
                        }
                    }
                }
            )
        }
      ) {
        BrandsGrid(
            subscriptionsState = subscriptions,
                onBrandClick = { brand ->
                    navController.navigate(Screen.BrandDetails.createRoute(brand.id.toString()))
                },
            onRetry = { viewModel.getSubscriptions() }
        )
      }
    }

    // Nested Brand Graph to share ViewModel/State
    navigation(
        startDestination = Screen.BrandDetails.route,
        route = "brand_graph/{brandId}"
    ) {
        composable(Screen.BrandDetails.route) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            BrandContainer(brandId, navController, Screen.BrandDetails.route, onLogout = { authViewModel.logout() })
        }
        composable(Screen.Wallet.route) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            BrandContainer(brandId, navController, Screen.Wallet.route, onLogout = { authViewModel.logout() })
        }
        composable(Screen.History.route) { backStackEntry ->
            val brandId = backStackEntry.arguments?.getString("brandId")
            BrandContainer(brandId, navController, Screen.History.route, onLogout = { authViewModel.logout() })
        }
    }
  }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun BrandContainer(
    brandId: String?,
    navController: NavHostController,
    currentRoute: String,
    onLogout: () -> Unit
) {
    // Get a global ViewModel to find the brand from subscriptions
    val globalViewModel = koinViewModel<BrandViewModel>(key = "global") { parametersOf("") }
    val subscriptions by globalViewModel.subscriptions.collectAsState()
    val subscriptionsState by globalViewModel.subscriptionsState.collectAsState()
    
    val subscription = subscriptions.find { it.company.id.toString() == brandId }
    
    LaunchedEffect(Unit) {
        if (subscriptions.isEmpty()) {
            globalViewModel.getSubscriptions()
        }
    }
    
    if (subscription == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (subscriptionsState.isLoading) {
                CircularProgressIndicator()
            } else if (subscriptionsState.error != null) {
                Text(text = subscriptionsState.error ?: "Error loading subscriptions", color = MaterialTheme.colorScheme.error)
            } else if (!subscriptionsState.isLoading && subscriptions.isEmpty()) {
                // If it's not loading and still empty after getSubscriptions, it might be an actual empty state
                 Text("No subscriptions found")
            }
        }
        return
    }
    
    val brand = subscription.company
    
    // Get the ViewModel scoped to this brandId
    val viewModel = koinViewModel<BrandViewModel>(key = brandId) { parametersOf(brandId) }
    
    LaunchedEffect(subscription.id) {
        viewModel.loadWallet(subscription.id)
        viewModel.getTransactions(subscription.id)
    }
    
    BrandMainScreen(
        brand = brand,
        currentRoute = currentRoute,
        navController = navController,
        viewModel = viewModel,
        onLogout = onLogout
    )
}
