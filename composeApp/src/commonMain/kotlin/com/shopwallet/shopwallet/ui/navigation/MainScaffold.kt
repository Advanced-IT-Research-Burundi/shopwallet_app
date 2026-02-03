package com.shopwallet.shopwallet.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import shopwallet.composeapp.generated.resources.Res
import shopwallet.composeapp.generated.resources.action_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
  title: String,
  showBackButton: Boolean,
  onBackClick: () -> Unit,
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  bottomBar: @Composable () -> Unit = {},
  content: @Composable () -> Unit
) {
  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
          if (showBackButton) {
            IconButton(onClick = onBackClick) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.action_back))
            }
          }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface,
          titleContentColor = MaterialTheme.colorScheme.onSurface
        )
      )
    },
    bottomBar = bottomBar,
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .padding(innerPadding)
        .consumeWindowInsets(innerPadding)
    ) {
      content()
    }
  }
}

@Composable
fun BottomNavBar(
  selectedRoute: String?,
  onItemSelected: (BottomNavScreen) -> Unit
) {
  Surface(
    color = MaterialTheme.colorScheme.background,
    shadowElevation = 8.dp,
    modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    NavigationBar(
      containerColor = Color.Transparent,
      tonalElevation = 0.dp,
      modifier = Modifier.height(64.dp),
      windowInsets = WindowInsets(0.dp)
    ) {
      val items = listOf(
        BottomNavScreen.Brand,
        BottomNavScreen.Wallet,
        BottomNavScreen.Cart,
        BottomNavScreen.History
      )

      items.forEach { screen ->
        val icon = when(screen) {
          BottomNavScreen.Brand -> Icons.Filled.Storefront
          BottomNavScreen.Wallet -> Icons.Filled.AccountBalanceWallet
          BottomNavScreen.Cart -> Icons.Filled.ShoppingCart
          BottomNavScreen.History -> Icons.Filled.History
        }
        
        NavigationBarItem(
          icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(22.dp)) },
          label = { 
            Text(
              text = stringResource(screen.labelRes), 
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              textAlign = TextAlign.Center
            ) 
          },
          selected = selectedRoute == screen.route,
          onClick = { onItemSelected(screen) },
          colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
          )
        )
      }
    }
  }
}
