package com.shopwallet.shopwallet.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun MyTextField(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  placeholder: String,
  leadingIcon: ImageVector? = null,
  trailing: ImageVector? = null,
  onTrailingClick: (() -> Unit)? = null,
  keyboardType: KeyboardType = KeyboardType.Text,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  imeAction: ImeAction = ImeAction.Done,
  onImeAction: (() -> Unit)? = null,
) {
  val colors = MaterialTheme.colorScheme
  val keyBoardController = LocalSoftwareKeyboardController.current

  Column {
    Text(label, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
    Spacer(Modifier.height(8.dp))

    OutlinedTextField(
      value = value,
      onValueChange = { newText ->
        onValueChange(newText)
      },
      placeholder = { Text(placeholder) },

      leadingIcon = leadingIcon?.let {
        { Icon(imageVector = it, contentDescription = null, tint = colors.primary) }
      },

      trailingIcon = trailing?.let {
        {
          IconButton(onClick = { onTrailingClick?.invoke() }) {
            Icon(imageVector = it, contentDescription = null, tint = colors.primary)
          }
        }
      },

      singleLine = true,
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth(),
      keyboardActions = KeyboardActions(onDone = {
        onImeAction?.invoke()
        keyBoardController?.hide()
      }),
      keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = colors.surfaceTint.copy(0.1f),
        unfocusedContainerColor = colors.surfaceTint.copy(0.1f),
        cursorColor = colors.primary,
        focusedBorderColor = colors.primary.copy(0.5f),
        unfocusedBorderColor = Color.Transparent
      ),
      visualTransformation = visualTransformation
    )
  }
}
