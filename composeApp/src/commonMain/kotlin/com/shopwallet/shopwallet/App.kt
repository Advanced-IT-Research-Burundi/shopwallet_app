package com.shopwallet.shopwallet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.shopwallet.shopwallet.ui.navigation.AppNavigation
import com.shopwallet.shopwallet.ui.theme.ShopWalletTheme

import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.shopwallet.shopwallet.data.local.AppPreferenceManager
import com.shopwallet.shopwallet.ui.viewmodel.SecurityViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.russhwolf.settings.Settings
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

@Composable
fun App(settings: Settings) {
  val navController = rememberNavController()
  val prefs = remember(settings) { AppPreferenceManager(settings) }
  
  val lifecycleOwner = LocalLifecycleOwner.current
  val securityViewModel: SecurityViewModel = viewModel { SecurityViewModel(prefs) }

  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_STOP) {
            securityViewModel.onAppBackgrounded()
        } else if (event == Lifecycle.Event.ON_RESUME) {
            securityViewModel.onAppForegrounded()
        }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }

  ShopWalletTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colorScheme.background
    ) {
      AppNavigation(navController, prefs, securityViewModel, onExit = {})
    }
  }
}