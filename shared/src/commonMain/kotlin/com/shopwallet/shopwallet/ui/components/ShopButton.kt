package com.shopwallet.shopwallet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.shopwallet.shopwallet.ui.theme.Shapes

enum class ShopButtonVariant {
  DEFAULT,
  DESTRUCTIVE,
  OUTLINE,
  SECONDARY,
  GHOST,
  LINK
}

@Composable
fun ShopButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  variant: ShopButtonVariant = ShopButtonVariant.DEFAULT,
  enabled: Boolean = true,
  shape: Shape = Shapes.small, // rounded-md (~8dp)
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  content: @Composable RowScope.() -> Unit
) {
  val colors = when (variant) {
    ShopButtonVariant.DEFAULT -> ButtonDefaults.buttonColors(
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = MaterialTheme.colorScheme.onPrimary
    )
    ShopButtonVariant.DESTRUCTIVE -> ButtonDefaults.buttonColors(
      containerColor = MaterialTheme.colorScheme.error,
      contentColor = MaterialTheme.colorScheme.onError
    )
    ShopButtonVariant.OUTLINE -> ButtonDefaults.outlinedButtonColors(
      containerColor = Color.Transparent, // bg-background
      contentColor = MaterialTheme.colorScheme.onBackground
    )
    ShopButtonVariant.SECONDARY -> ButtonDefaults.buttonColors(
      containerColor = MaterialTheme.colorScheme.secondary,
      contentColor = MaterialTheme.colorScheme.onSecondary
    )
    ShopButtonVariant.GHOST -> ButtonDefaults.textButtonColors(
      contentColor = MaterialTheme.colorScheme.onSurface
    )
    ShopButtonVariant.LINK -> ButtonDefaults.textButtonColors(
      contentColor = MaterialTheme.colorScheme.primary
    )
  }

  val border = if (variant == ShopButtonVariant.OUTLINE) {
    BorderStroke(1.dp, MaterialTheme.colorScheme.outline) // border-input
  } else null

  val elevation = if (variant == ShopButtonVariant.DEFAULT || variant == ShopButtonVariant.DESTRUCTIVE) {
    ButtonDefaults.buttonElevation() // Default elevation
  } else {
    ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp)
  }

  // Default h-9 (36dp) to h-10 (40dp) mostly
  // h-12 in AuthScreen implies 48dp

  Button(
    onClick = onClick,
    modifier = modifier.defaultMinSize(minHeight = 40.dp),
    enabled = enabled,
    shape = shape,
    colors = colors,
    elevation = elevation,
    border = border,
    contentPadding = contentPadding,
    content = content
  )
}
