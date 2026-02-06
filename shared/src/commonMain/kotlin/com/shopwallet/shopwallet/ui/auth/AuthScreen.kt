package com.shopwallet.shopwallet.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.shopwallet.shopwallet.ui.components.ShopButton
import com.shopwallet.shopwallet.ui.components.ShopButtonVariant
import com.shopwallet.shopwallet.ui.components.ShopInput

import com.shopwallet.shopwallet.ui.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.collectAsState
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit
) {
    val viewModel = koinViewModel<AuthViewModel>()
    val otpRequestState by viewModel.otpRequestState.collectAsState()
    val verifyOtpState by viewModel.verifyOtpState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var step by remember { mutableStateOf(AuthStep.PHONE) }
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }

    // Navigate on successful verification
    LaunchedEffect(verifyOtpState.data) {
        if (verifyOtpState.data != null) {
            onAuthenticated()
        }
    }
    
    // Move to OTP step on successful request
    LaunchedEffect(otpRequestState.data) {
        if (otpRequestState.data?.success == true) {
            step = AuthStep.OTP
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Error Message
            val errorMessage = if (step == AuthStep.PHONE) otpRequestState.error else verifyOtpState.error
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
                    val isLoading = if (step == AuthStep.PHONE) otpRequestState.isLoading else verifyOtpState.isLoading
                    if (isLoading) {
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
                        if (phoneNumber.length == 8) {
                            viewModel.requestOtp(phoneNumber)
                        }
                    },
                    isLoading = otpRequestState.isLoading
                )
            } else {
                OtpInputStep(
                    otpCode = otpCode,
                    onOtpChange = { otpCode = it },
                    onVerify = {
                        if (otpCode.length == 6) {
                            viewModel.verifyOtp(phoneNumber, otpCode)
                        }
                    },
                    onChangePhone = { 
                        step = AuthStep.PHONE
                        otpCode = ""
                    },
                    isLoading = verifyOtpState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(48.dp)) // Spacing for footer

            Text(
                text = "By continuing, you agree to our Terms of Service and Privacy Policy",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp), // text-xs
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
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
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium), // text-sm font-medium
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        ShopInput(
            value = phoneNumber,
            onValueChange = { if (it.length <= 8 && it.all { char -> char.isDigit() }) onPhoneChange(it) },
            placeholder = "+257 00 000 000",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.height(56.dp) // h-12 = 48px, but inputs are usually comfy at 56
        )
        Spacer(modifier = Modifier.height(16.dp))
        ShopButton(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            enabled = phoneNumber.length == 8 && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Continue")
            }
        }
    }
}

@Composable
fun OtpInputStep(
    otpCode: String,
    onOtpChange: (String) -> Unit,
    onVerify: () -> Unit,
    onChangePhone: () -> Unit,
    isLoading: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Verification Code",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Simple OTP Input Implementation
        // In the future: Replicate InputOTP slots. For now, a centered high-spacing input.
        ShopInput(
            value = otpCode,
            onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) onOtpChange(it) },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                letterSpacing = 8.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            placeholder = "000000",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        ShopButton(
            onClick = onVerify,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            enabled = otpCode.length == 6 && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Verify & Continue")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ShopButton(
            onClick = onChangePhone,
            variant = ShopButtonVariant.GHOST,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change phone number", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

enum class AuthStep {
    PHONE, OTP
}
