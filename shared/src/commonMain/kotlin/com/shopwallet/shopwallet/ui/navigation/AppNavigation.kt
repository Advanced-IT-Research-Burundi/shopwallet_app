package com.shopwallet.shopwallet.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.shopwallet.shopwallet.ui.auth.AuthScreen
import com.shopwallet.shopwallet.ui.auth.OtpScreen
import com.shopwallet.shopwallet.ui.screens.BrandMainScreen
import com.shopwallet.shopwallet.ui.screens.BrandsGrid
import com.shopwallet.shopwallet.ui.viewmodel.AuthViewModel
import com.shopwallet.shopwallet.ui.viewmodel.BrandViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AppNavigation() {
    val authViewModel = koinViewModel<AuthViewModel>()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val logoutState by authViewModel.logoutState.collectAsState()
    val initialIsLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val startDestination = if (initialIsLoggedIn) Screen.Brands else Screen.Auth
    val navigator = remember { AppNavigator(startDestination) }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn && !initialIsLoggedIn) {
            navigator.navigate(Screen.Brands, clearStack = true)
        } else if (!isLoggedIn && (initialIsLoggedIn || logoutState.data != null)) {
            authViewModel.resetStates()
            navigator.navigate(Screen.Auth, clearStack = true)
        }
    }

    Crossfade(targetState = navigator.currentScreen) { currentScreen ->
        when (currentScreen) {
            is Screen.Auth -> {
                AuthScreen(onOtpRequested = { phone ->
                    navigator.navigate(Screen.Otp(phone))
                })
            }
            is Screen.Otp -> {
                OtpScreen(
                    phone = currentScreen.phone,
                    onAuthenticated = {
                        navigator.navigate(Screen.Brands, clearStack = true)
                    },
                    onBack = {
                        navigator.popBackStack()
                    }
                )
            }
            is Screen.Brands -> {
                val viewModel = koinViewModel<BrandViewModel>(key = "global") { parametersOf("") }
                val subscriptions by viewModel.subscriptionsState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.getSubscriptions()
                }

                MainScaffold(
                    title = "ShopWallet",
                    onLogout = {
                        authViewModel.logout()
                        navigator.navigate(Screen.Auth, clearStack = true)
                    },
                    onFabClick = {
                        navigator.navigate(Screen.Brands, clearStack = true)
                    },
                    bottomBar = {
                        BottomNavBar(
                            selectedRoute = BottomNavScreen.Brand,
                            onItemSelected = { screen ->
                                if (screen == BottomNavScreen.Brand) {
                                    navigator.navigate(Screen.Brands, clearStack = true)
                                } else {
                                    val defaultBrandId = viewModel.subscriptions.value.firstOrNull()?.company?.id?.toString() ?: ""
                                    if (defaultBrandId.isNotEmpty()) {
                                        when (screen) {
                                            is BottomNavScreen.Wallet -> navigator.navigate(Screen.Wallet(defaultBrandId))
                                            is BottomNavScreen.History -> navigator.navigate(Screen.History(defaultBrandId))
                                            else -> {}
                                        }
                                    }
                                }
                            }
                        )
                    }
                ) {
                    BrandsGrid(
                        subscriptionsState = subscriptions,
                        onBrandClick = { brand ->
                            navigator.navigate(Screen.Wallet(brand.id.toString()))
                        },
                        onRetry = { viewModel.getSubscriptions() }
                    )
                }
            }
            is Screen.BrandDetails -> {
                BrandContainer(currentScreen.brandId, navigator, currentScreen)
            }
            is Screen.Wallet -> {
                BrandContainer(currentScreen.brandId, navigator, currentScreen)
            }
            is Screen.History -> {
                BrandContainer(currentScreen.brandId, navigator, currentScreen)
            }
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun BrandContainer(
    brandId: String?,
    navigator: AppNavigator,
    currentScreen: Screen,
) {
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
                Text("No subscriptions found")
            }
        }
        return
    }

    val brand = subscription.company
    val viewModel = koinViewModel<BrandViewModel>(key = brandId) { parametersOf(brandId) }

    LaunchedEffect(subscription.id) {
        viewModel.loadWallet(subscription.id)
        viewModel.getTransactions(subscription.id)
    }

    BrandMainScreen(
        brand = brand,
        currentScreen = currentScreen,
        navigator = navigator,
        viewModel = viewModel,
    )
}
