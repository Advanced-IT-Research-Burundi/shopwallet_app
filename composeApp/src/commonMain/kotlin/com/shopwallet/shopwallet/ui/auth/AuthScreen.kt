package com.shopwallet.shopwallet.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthScreen(
  onAuthenticated: () -> Unit
) {
  var step by remember { mutableStateOf(AuthStep.PHONE) }
  var phoneNumber by remember { mutableStateOf("") }
  var otpCode by remember { mutableStateOf("") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // Logo & Title
    Surface(
      modifier = Modifier.size(64.dp),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.primary
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = Icons.Default.PhoneAndroid,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier.size(32.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Shop Wallet",
      style = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
      )
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = if (step == AuthStep.PHONE) "Enter your phone number to get started" else "Enter the verification code sent to your phone",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(32.dp))

    if (step == AuthStep.PHONE) {
      PhoneInputStep(
        phoneNumber = phoneNumber,
        onPhoneChange = { phoneNumber = it },
        onContinue = {
          if (phoneNumber.length >= 10) {
            step = AuthStep.OTP
          }
        }
      )
    } else {
      OtpInputStep(
        otpCode = otpCode,
        onOtpChange = { otpCode = it },
        onVerify = {
          // Mimic verification: accept any OTP
          if (otpCode.length == 6) {
            onAuthenticated()
          }
        },
        onChangePhone = { step = AuthStep.PHONE }
      )
    }

    Spacer(modifier = Modifier.weight(1f))

    Text(
      text = "By continuing, you agree to our Terms of Service and Privacy Policy",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(bottom = 16.dp)
    )
  }
}

@Composable
fun PhoneInputStep(
  phoneNumber: String,
  onPhoneChange: (String) -> Unit,
  onContinue: () -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = "Phone Number",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
      value = phoneNumber,
      onValueChange = { if (it.all { char -> char.isDigit() }) onPhoneChange(it) },
      modifier = Modifier.fillMaxWidth(),
      placeholder = { Text("+1 (555) 000-0000") },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
      singleLine = true
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
      onClick = onContinue,
      modifier = Modifier.fillMaxWidth().height(48.dp),
      enabled = phoneNumber.length >= 10
    ) {
      Text("Continue")
    }
  }
}

@Composable
fun OtpInputStep(
  otpCode: String,
  onOtpChange: (String) -> Unit,
  onVerify: () -> Unit,
  onChangePhone: () -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = "Verification Code",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Medium,
      textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Simple OTP Input Implementation
    OutlinedTextField(
      value = otpCode,
      onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) onOtpChange(it) },
      modifier = Modifier.fillMaxWidth(),
      textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 24.sp, letterSpacing = 8.sp),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
      singleLine = true,
      placeholder = { Text("000000", fontSize = 24.sp, letterSpacing = 8.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
      onClick = onVerify,
      modifier = Modifier.fillMaxWidth().height(48.dp),
      enabled = otpCode.length == 6
    ) {
      Text("Verify & Continue")
    }

    Spacer(modifier = Modifier.height(16.dp))

    TextButton(onClick = onChangePhone) {
      Text("Change phone number", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

enum class AuthStep {
  PHONE, OTP
}
