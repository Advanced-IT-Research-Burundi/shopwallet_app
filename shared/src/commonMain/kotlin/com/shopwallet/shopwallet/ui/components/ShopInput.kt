package com.shopwallet.shopwallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
        color = MaterialTheme.colorScheme.onBackground
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
  placeholder: String = "",
  keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier.fillMaxWidth(),
    placeholder = {
      Text(
        text = placeholder,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
      )
    },
    trailingIcon = {
      Box(modifier = Modifier.padding(end = 4.dp)) {
        if (loading) {
          CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.primary
          )
        } else {
          val isEnabled = value.isNotEmpty()
          Surface(
            onClick = onPostClick,
            enabled = isEnabled,
            shape = RoundedCornerShape(12.dp),
            color = if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(40.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Submit",
                tint = if (isEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }
    },
    textStyle = MaterialTheme.typography.bodyLarge.copy(
      fontWeight = FontWeight.SemiBold,
      fontSize = 18.sp
    ),
    keyboardOptions = keyboardOptions,
    singleLine = true,
    shape = RoundedCornerShape(16.dp),
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = MaterialTheme.colorScheme.primary,
      unfocusedBorderColor = MaterialTheme.colorScheme.outline,
      focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
      unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
      cursorColor = MaterialTheme.colorScheme.primary,
      focusedLabelColor = MaterialTheme.colorScheme.primary,
      unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
  )
}
