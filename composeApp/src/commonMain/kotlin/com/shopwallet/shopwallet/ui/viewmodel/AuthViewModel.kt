package com.shopwallet.shopwallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopwallet.shopwallet.data.model.OtpResponse
import com.shopwallet.shopwallet.data.model.UserResponse
import com.shopwallet.shopwallet.data.model.VerifyOtpResponse
import com.shopwallet.shopwallet.data.repository.AuthRepo
import com.shopwallet.shopwallet.utils.UiState
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
        viewModelScope.launch {
            _otpRequestState.value = UiState(isLoading = true)
            authRepo.requestOtp(phone).fold(
                onSuccess = { response ->
                    _otpRequestState.value = UiState(data = response)
                },
                onFailure = { error ->
                    _otpRequestState.value = UiState(error = error.message ?: "Failed to request OTP")
                }
            )
        }
    }

    fun verifyOtp(phone: String, otp: String) {
        viewModelScope.launch {
            _verifyOtpState.value = UiState(isLoading = true)
            authRepo.verifyOtp(phone, otp).fold(
                onSuccess = { response ->
                    _verifyOtpState.value = UiState(data = response)
                    _currentUser.value = response.user
                },
                onFailure = { error ->
                    _verifyOtpState.value = UiState(error = error.message ?: "Invalid OTP")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepo.logout()
            _currentUser.value = null
        }
    }

    fun isLoggedIn(): Boolean = authRepo.getToken() != null
}
