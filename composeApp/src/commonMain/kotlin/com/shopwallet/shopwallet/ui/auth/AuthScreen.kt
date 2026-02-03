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
import com.shopwallet.shopwallet.ui.components.ShopButton
import com.shopwallet.shopwallet.ui.components.ShopButtonVariant
import com.shopwallet.shopwallet.ui.components.ShopInput

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit
) {
    var step by remember { mutableStateOf(AuthStep.PHONE) }
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp) // Max-w-sm in React ~ 24rem = 384px.
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo & Title
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp), // rounded-2xl
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

            Spacer(modifier = Modifier.height(16.dp)) // mb-4 on logo div + space-y-3

            Text(
                text = "Shop Wallet",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    letterSpacing = (-0.5).sp // tracking-tight
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (step == AuthStep.PHONE) "Enter your phone number to get started" else "Enter the verification code sent to your phone",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, // text-muted-foreground
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp)) // space-y-8 separates header from inputs

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
    onContinue: () -> Unit
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
            onValueChange = { if (it.all { char -> char.isDigit() }) onPhoneChange(it) },
            placeholder = "+1 (555) 000-0000",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.height(56.dp) // h-12 = 48px, but inputs are usually comfy at 56
        )
        Spacer(modifier = Modifier.height(16.dp))
        ShopButton(
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
            enabled = otpCode.length == 6
        ) {
            Text("Verify & Continue")
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
