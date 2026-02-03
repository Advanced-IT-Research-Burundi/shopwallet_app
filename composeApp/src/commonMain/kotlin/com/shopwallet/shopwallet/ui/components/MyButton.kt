package org.awala.awala.presentation.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shopwallet.shopwallet.ui.components.LoadingDots

@Composable
fun PrimaryButton(
  text: String,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  loading: Boolean = false,
  icon: ImageVector? = null,
  shapeRadius: Int = 20,
  onClick: () -> Unit
) {
  val colors = MaterialTheme.colorScheme
  Button(
    onClick = onClick,
    enabled = enabled,
    colors = ButtonDefaults.buttonColors(
      containerColor = if (enabled) colors.primary
      else colors.surfaceVariant,
      contentColor = if (enabled) colors.onPrimary
      else colors.onSurfaceVariant
    ),
    modifier = modifier
      .fillMaxWidth()
      .height(48.dp),
    shape = RoundedCornerShape(shapeRadius.dp)
  ) {
    Row(
      modifier = Modifier,
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      icon?.let {
        Icon(
          imageVector = it,
          contentDescription = text,
          modifier = Modifier.size(16.dp),
          tint = colors.onPrimary
        )
      }
      when {
        loading -> LoadingDots(color = colors.onPrimary)
        else ->
          Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = colors.onPrimary
          )
      }
    }
  }
}

@Composable
fun SecondaryButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  loading: Boolean = false,
  icon: ImageVector? = null,
  shapeRadius: Int = 20
) {
  val colors = MaterialTheme.colorScheme
  OutlinedButton(
    onClick = onClick,
    enabled = enabled,
    border = BorderStroke(1.dp, colors.primary),
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = colors.surface,
      contentColor = colors.primary
    ),
    shape = RoundedCornerShape(shapeRadius.dp),
    modifier = modifier
      .fillMaxWidth()
      .height(48.dp)
  ) {
    Row(
      modifier = Modifier,
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      icon?.let {
        Icon(it, text,Modifier.size(16.dp))
      }
      when {
        loading -> LoadingDots()
        else ->
          Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = colors.primary
          )
      }
    }
  }
}

@Composable
fun ToggleButton(
  title: String,
  isActive: Boolean,
  activeColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = MaterialTheme.colorScheme
  Box(
    modifier = modifier
      .clip(CircleShape)
      .background(if (isActive) colors.primary else Color.Transparent)
      .clickable(onClick = onClick)
      .padding(vertical = 12.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      title,
      color = if (isActive) activeColor else colors.primary,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Medium
    )
  }
}

@Composable
fun BackButton(
  modifier: Modifier = Modifier,
  onBack: () -> Unit,
) {
  val colors =MaterialTheme.colorScheme
  IconButton(
    onClick = onBack,
    modifier = modifier
  ) {
    Card(
      shape = CircleShape,
      elevation = CardDefaults.cardElevation(10.dp),
      colors = CardDefaults.cardColors(
        containerColor = colors.background,
        contentColor = colors.onBackground
      )
    ) {
      Icon(
        imageVector = Icons.Filled.ChevronLeft,
        contentDescription = null,
        tint = colors.primary,
        modifier = Modifier.padding(4.dp)
      )
    }
  }
}



@Composable
fun SmallButton(
  text: String,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  loading: Boolean = false,
  icon: ImageVector? = null,
  shapeRadius: Int = 20,
  onClick: () -> Unit
){
  val colors = MaterialTheme.colorScheme
  Button(
    onClick = onClick,
    enabled = enabled,
    colors = ButtonDefaults.buttonColors(
      containerColor = if (enabled) colors.background else colors.surfaceVariant,
      contentColor = if (enabled) colors.primary else colors.onSurfaceVariant
    ),
    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
    border = if (enabled) BorderStroke(1.dp, colors.primary) else null,
    modifier = modifier.height(30.dp),
    shape = RoundedCornerShape(shapeRadius.dp)
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      icon?.let {
        Icon(
          imageVector = it,
          contentDescription = text,
          modifier = Modifier.size(14.dp),
          tint = colors.primary
        )
      }
      when {
        loading -> LoadingDots(color = colors.primary)
        else ->
          Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = colors.primary
          )
      }
    }
  }
}