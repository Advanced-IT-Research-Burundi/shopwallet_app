package com.shopwallet.shopwallet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.shopwallet.shopwallet.ui.theme.Shapes

@Composable
fun ShopInput(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  placeholder: String? = null,
  label: String? = null,
  enabled: Boolean = true,
  readOnly: Boolean = false,
  textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  isError: Boolean = false,
  maxLines: Int = 1,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null
) {
  // React input tokens:
  // border-input (Outline)
  // bg-input-background (Surface/White)
  // rounded-md (Small shape)
  // focus-visible:ring-ring (Primary)

  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier.fillMaxWidth(),
    enabled = enabled,
    readOnly = readOnly,
    textStyle = textStyle,
    label = if (label != null) { { Text(label) } } else null,
    placeholder = if (placeholder != null) { { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) } } else null,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
    isError = isError,
    visualTransformation = visualTransformation,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    maxLines = maxLines,
    shape = Shapes.small, // rounded-md
    colors = OutlinedTextFieldDefaults.colors(
      focusedContainerColor = MaterialTheme.colorScheme.surface,
      unfocusedContainerColor = MaterialTheme.colorScheme.surface,
      disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
      focusedBorderColor = MaterialTheme.colorScheme.primary, // ring
      unfocusedBorderColor = MaterialTheme.colorScheme.outline, // border-input
      cursorColor = MaterialTheme.colorScheme.primary,
      errorBorderColor = MaterialTheme.colorScheme.error,
    )
  )
}
