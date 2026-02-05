package com.shopwallet.shopwallet.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shopwallet.shopwallet.ui.viewmodel.AuthViewModel
import com.shopwallet.shopwallet.utils.UiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OtpScreen(
    onNavigateToMain: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var otpStep by remember { mutableStateOf(false) } // false = phone input, true = otp input
    
    val otpRequestState by viewModel.otpRequestState.collectAsState()
    val verifyOtpState by viewModel.verifyOtpState.collectAsState()
    
    // Navigate on successful verification
    LaunchedEffect(verifyOtpState.data) {
        if (verifyOtpState.data != null) {
            onNavigateToMain()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = if (!otpStep) "Enter Phone Number" else "Enter OTP Code",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = if (!otpStep) 
                "We'll send you a verification code" 
            else 
                "We sent a code to $phone",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        if (!otpStep) {
            // Phone input step
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                placeholder = { Text("257 XX XXX XXX") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    viewModel.requestOtp(phone)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = phone.isNotBlank() && !otpRequestState.isLoading
            ) {
                if (otpRequestState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Request OTP")
                }
            }
            
            // Show success and move to OTP step
            LaunchedEffect(otpRequestState.data) {
                if (otpRequestState.data?.success == true) {
                    otpStep = true
                }
            }
        } else {
            // OTP input step
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("OTP Code") },
                placeholder = { Text("XXXXXX") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.verifyOtp(phone, otp)
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    viewModel.verifyOtp(phone, otp)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = otp.isNotBlank() && !verifyOtpState.isLoading
            ) {
                if (verifyOtpState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Verify OTP")
                }
            }
            
            TextButton(
                onClick = {
                    otpStep = false
                    otp = ""
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Change Phone Number")
            }
        }
        
        // Error messages
        if (otpRequestState.error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = otpRequestState.error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
        
        if (verifyOtpState.error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = verifyOtpState.error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}
