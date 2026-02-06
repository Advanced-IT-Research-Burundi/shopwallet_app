package com.shopwallet.shopwallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopwallet.shopwallet.data.model.OtpResponse
import com.shopwallet.shopwallet.data.model.UserResponse
import com.shopwallet.shopwallet.data.model.VerifyOtpResponse
import com.shopwallet.shopwallet.data.repository.AuthRepo
import com.shopwallet.shopwallet.utils.UiState
import com.shopwallet.shopwallet.utils.launchWithState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepo: AuthRepo
) : androidx.lifecycle.ViewModel() {

    private val _otpRequestState = MutableStateFlow(UiState<OtpResponse>())
    val otpRequestState = _otpRequestState.asStateFlow()

    private val _verifyOtpState = MutableStateFlow(UiState<VerifyOtpResponse>())
    val verifyOtpState = _verifyOtpState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserResponse?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun requestOtp(phone: String) {
        launchWithState(
            stateFlow = _otpRequestState,
            block = { authRepo.requestOtp(phone) },
            onSuccess = { resp ->
                _otpRequestState.value = UiState(data = resp)
            },
            onFailure = { error ->
                _otpRequestState.value = UiState(error = error.message ?: "Failed to request OTP")
            }
        )
    }

    fun verifyOtp(phone: String, otp: String) {
        launchWithState(
            stateFlow = _verifyOtpState,
            block = { authRepo.verifyOtp(phone, otp) },
            onSuccess = { resp ->
                _verifyOtpState.value = UiState(data = resp)
                _currentUser.value = resp.user
            },
            onFailure = { error ->
                _verifyOtpState.value = UiState(error = error.message ?: "Failed to verify OTP")
            }
        )
    }

    fun logout() {
        viewModelScope.launch {
            authRepo.logout()
            _currentUser.value = null
        }
    }

    fun isLoggedIn(): Boolean = authRepo.getToken() != null
}
