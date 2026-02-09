package com.shopwallet.shopwallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun ShopOtpInput(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  length: Int = 6,
  isError: Boolean = false
) {
  BasicTextField(
    value = value,
    onValueChange = {
      if (it.length <= length && it.all { char -> char.isDigit() }) {
        onValueChange(it)
      }
    },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    modifier = modifier,
    decorationBox = {
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
      ) {
        repeat(length) { index ->
          val char = value.getOrNull(index)?.toString() ?: ""
          val isFocused = index == value.length
          OtpCell(
            char = char,
            isFocused = isFocused,
            isError = isError
          )
        }
      }
    }
  )
}

@Composable
private fun OtpCell(
  char: String,
  isFocused: Boolean,
  isError: Boolean
) {
  val borderColor = when {
    isError -> MaterialTheme.colorScheme.error
    isFocused -> MaterialTheme.colorScheme.primary
    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
  }

  Box(
    modifier = Modifier
      .size(width = 48.dp, height = 56.dp)
      .border(if (isFocused || isError) 2.dp else 1.dp, borderColor, RoundedCornerShape(8.dp))
      .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = char,
      style = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Bold,
        color = Color.Black
      )
    )
  }
}

@Composable
fun ShopPostInput(
  value: String,
  onValueChange: (String) -> Unit,
  onPostClick: () -> Unit,
  loading: Boolean = false,
  modifier: Modifier = Modifier,
  placeholder: String = ""
) {
  Column(modifier = modifier) {
    Row(
      verticalAlignment = Alignment.Bottom,
      modifier = Modifier.fillMaxWidth()
    ) {
      BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.weight(1f),
        textStyle = TextStyle(
          fontSize = 24.sp,
          color = MaterialTheme.colorScheme.onBackground,
          fontWeight = FontWeight.Medium
        ),
        decorationBox = { innerTextField ->
          if (value.isEmpty()) {
            Text(
              text = placeholder,
              color = Color.LightGray,
              fontSize = 24.sp
            )
          }
          innerTextField()
        }
      )

      if (loading) {
        CircularProgressIndicator(
          modifier = Modifier.size(20.dp),
          color = MaterialTheme.colorScheme.onPrimary
        )
      } else {
        IconButton(onClick = onPostClick) {
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Post",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }
    }
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(2.dp)
        .background(MaterialTheme.colorScheme.outline)
    )
  }
}
