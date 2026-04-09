package com.shopwallet.shopwallet.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource

/**
 * Custom shape for the BottomAppBar with a smooth concave cutout at the top center.
 * Uses Bézier curves (cubicTo) for a fluid and state-of-the-art transition.
 */
class CustomBottomBarShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ): androidx.compose.ui.graphics.Outline {
        return androidx.compose.ui.graphics.Outline.Generic(Path().apply {
            val notchWidth = 110.dp.value * density.density
            val notchHeight = 36.dp.value * density.density
            val cornerRadius = 20.dp.value * density.density
            val centerX = size.width / 2

            // Top-left corner
            moveTo(0f, cornerRadius)
            quadraticTo(0f, 0f, cornerRadius, 0f)
            
            // Side line to the start of the notch
            lineTo(centerX - notchWidth * 0.65f, 0f)
            
            // Smooth concave notch using Cubic Bézier curves for a premium look
            cubicTo(
                centerX - notchWidth * 0.40f, 0f,
                centerX - notchWidth * 0.35f, notchHeight,
                centerX, notchHeight
            )
            cubicTo(
                centerX + notchWidth * 0.35f, notchHeight,
                centerX + notchWidth * 0.40f, 0f,
                centerX + notchWidth * 0.65f, 0f
            )
            
            // Side line to the top-right corner
            lineTo(size.width - cornerRadius, 0f)
            quadraticTo(size.width, 0f, size.width, cornerRadius)
            
            // Bottom part of the bar
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
  title: String,
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  actions: @Composable RowScope.() -> Unit = {},
  onLogout: () -> Unit = {},
  bottomBar: @Composable () -> Unit = {},
  onFabClick: () -> Unit = {},
  content: @Composable () -> Unit
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { 
            Text(
                title, 
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            ) 
        },
        actions = {
            actions()
            Surface(
                onClick = onLogout,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Box(
                    modifier = Modifier.padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout, 
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
          titleContentColor = MaterialTheme.colorScheme.onBackground
        )
      )
    },
    bottomBar = bottomBar,
    floatingActionButton = {
        if (bottomBar != @Composable {}) {
            FloatingActionButton(
                onClick = onFabClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.size(52.dp).offset(y = 44.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Storefront,
                    contentDescription = "Brands",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    },
    floatingActionButtonPosition = FabPosition.Center,
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .padding(innerPadding)
        .consumeWindowInsets(innerPadding)
        .fillMaxSize()
    ) {
      content()
    }
  }
}

@Composable
fun BottomNavBar(
  selectedRoute: BottomNavScreen?,
  onItemSelected: (BottomNavScreen) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(68.dp)
            .shadow(elevation = 12.dp, shape = CustomBottomBarShape()),
        shape = CustomBottomBarShape(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LEFT ELEMENT
            NavBarItem(
                screen = BottomNavScreen.Wallet,
                isSelected = selectedRoute is BottomNavScreen.Wallet,
                onClick = { onItemSelected(BottomNavScreen.Wallet) }
            )

            // DYNAMIC SPACE FOR CENTER FAB
            Spacer(modifier = Modifier.width(64.dp))

            // RIGHT ELEMENT
            NavBarItem(
                screen = BottomNavScreen.History,
                isSelected = selectedRoute is BottomNavScreen.History,
                onClick = { onItemSelected(BottomNavScreen.History) }
            )
        }
    }
}

@Composable
private fun NavBarItem(
    screen: BottomNavScreen,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    
    val icon = when (screen) {
        BottomNavScreen.Wallet -> Icons.Filled.AccountBalanceWallet
        BottomNavScreen.History -> Icons.Filled.History
        else -> Icons.Filled.Storefront
    }

    Column(
        modifier = Modifier
            .width(60.dp)
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = stringResource(screen.labelRes),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 11.sp
            ),
            color = if (isSelected) activeColor else inactiveColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
