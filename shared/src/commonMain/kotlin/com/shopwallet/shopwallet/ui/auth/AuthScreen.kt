package com.shopwallet.shopwallet.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopwallet.shopwallet.ui.components.ShopOtpInput
import com.shopwallet.shopwallet.ui.components.ShopPostInput
import com.shopwallet.shopwallet.ui.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AuthScreen(
  onOtpRequested: (String) -> Unit
) {
  val viewModel = koinViewModel<AuthViewModel>()
  val otpRequestState by viewModel.otpRequestState.collectAsState()
  val phoneNumber by viewModel.phoneNumber.collectAsState()

  LaunchedEffect(Unit) {
    viewModel.resetStates()
  }

  LaunchedEffect(otpRequestState.data) {
    if (otpRequestState.data != null) {
      onOtpRequested(phoneNumber)
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .imePadding() // Automatically adds padding for the keyboard
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth(),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          // Error Message
          val errorMessage = otpRequestState.error
          if (errorMessage != null) {
            Text(
              text = errorMessage,
              color = MaterialTheme.colorScheme.error,
              style = MaterialTheme.typography.bodySmall,
              modifier = Modifier.padding(bottom = 16.dp)
            )
          }

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
              fontSize = 28.sp,
              letterSpacing = (-0.5).sp
            ),
            color = MaterialTheme.colorScheme.onBackground
          )

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Enter your phone number to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(32.dp))

          PhoneInputStep(
            phoneNumber = phoneNumber,
            onPhoneChange = { viewModel.setPhone(it) },
            onContinue = {
              if (phoneNumber.length == 8) {
                viewModel.requestOtp()
              }
            },
            isLoading = otpRequestState.isLoading
          )

          Spacer(modifier = Modifier.height(48.dp))

          Text(
            text = "By continuing, you agree to our Terms of Service and Privacy Policy",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun OtpScreen(
  phone: String,
  onAuthenticated: () -> Unit,
  onBack: () -> Unit
) {
  val viewModel = koinViewModel<AuthViewModel>()
  val verifyOtpState by viewModel.verifyOtpState.collectAsState()
  val otpRequestState by viewModel.otpRequestState.collectAsState()
  var otpCode by remember { mutableStateOf("") }

  LaunchedEffect(Unit) {
    viewModel.resetStates()
  }

  LaunchedEffect(verifyOtpState.data) {
    if (verifyOtpState.data != null) {
      onAuthenticated()
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .imePadding()
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth(),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          if (verifyOtpState.error != null) {
            Text(
              text = verifyOtpState.error ?: "",
              color = MaterialTheme.colorScheme.error,
              style = MaterialTheme.typography.bodySmall,
              modifier = Modifier.padding(bottom = 16.dp)
            )
          }

          Surface(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary
          ) {
            Box(contentAlignment = Alignment.Center) {
              if (verifyOtpState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
              } else {
                Icon(
                  imageVector = Icons.Default.PhoneAndroid,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onPrimary,
                  modifier = Modifier.size(32.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Verification",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.SemiBold,
              fontSize = 28.sp,
              letterSpacing = (-0.5).sp
            ),
            color = MaterialTheme.colorScheme.onBackground
          )

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Enter the verification code sent to \n+257 $phone",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(32.dp))

          OtpInputStep(
            otpCode = otpCode,
            onOtpChange = { otpCode = it },
            onVerify = {
              if (otpCode.length == 6) {
                viewModel.verifyOtp(phone, otpCode)
              }
            },
            isLoading = verifyOtpState.isLoading
          )

          Spacer(modifier = Modifier.height(48.dp))

          Text(
            text = if (otpRequestState.isLoading) "Requesting code..." else "Didn't receive the code? Resend",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, color = MaterialTheme.colorScheme.primary),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp).clickable {
              viewModel.requestOtp()
            }
          )
        }
      }
    }
  }
}

@Composable
fun PhoneInputStep(
  phoneNumber: String,
  onPhoneChange: (String) -> Unit,
  onContinue: () -> Unit,
  isLoading: Boolean = false
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = "Phone Number",
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
      color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(8.dp))
    ShopPostInput(
      value = phoneNumber,
      loading = isLoading,
      onValueChange = { if (it.length <= 8 && it.all { char -> char.isDigit() }) onPhoneChange(it) },
      onPostClick = onContinue,
      placeholder = "00 000 000"
    )
  }
}

@Composable
fun OtpInputStep(
  otpCode: String,
  onOtpChange: (String) -> Unit,
  onVerify: () -> Unit,
  isLoading: Boolean = false
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("Verification Code", style = MaterialTheme.typography.titleMedium)

    Spacer(modifier = Modifier.height(24.dp))

    ShopOtpInput(
      value = otpCode,
      onValueChange = onOtpChange,
      length = 6
    )

    Spacer(modifier = Modifier.height(32.dp))

    Button(
      onClick = onVerify,
      enabled = otpCode.length == 6 && !isLoading,
      modifier = Modifier.fillMaxWidth().height(56.dp),
      colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
      shape = RoundedCornerShape(12.dp)
    ) {
      if (isLoading) CircularProgressIndicator(color = Color.White)
      else Text("Verify & Continue", color = MaterialTheme.colorScheme.onPrimary)
    }
  }
}

