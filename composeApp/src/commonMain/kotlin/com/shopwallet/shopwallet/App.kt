package com.shopwallet.shopwallet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.shopwallet.shopwallet.ui.navigation.AppNavigation
import com.shopwallet.shopwallet.ui.theme.ShopWalletTheme
import com.shopwallet.shopwallet.ui.viewmodel.SecurityViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
  val navController = rememberNavController()
  val securityViewModel = koinViewModel<SecurityViewModel>()
  
  val lifecycleOwner = LocalLifecycleOwner.current
  val isLocked by securityViewModel.isLocked.collectAsState()

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
      AppNavigation(navController, onExit = {})
    }
  }
}