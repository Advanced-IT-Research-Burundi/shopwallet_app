package com.shopwallet.shopwallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopwallet.shopwallet.data.model.OtpResponse
import com.shopwallet.shopwallet.data.model.UserResponse
import com.shopwallet.shopwallet.data.model.VerifyOtpResponse
import com.shopwallet.shopwallet.data.remote.EmptyResponse
import com.shopwallet.shopwallet.data.repository.AuthRepo
import com.shopwallet.shopwallet.utils.UiState
import com.shopwallet.shopwallet.utils.launchWithState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
  private val authRepo: AuthRepo
) : ViewModel() {
  private val _isLoggedIn = MutableStateFlow(authRepo.getToken() != null)
  val isLoggedIn = _isLoggedIn.asStateFlow()

  private val _phoneNumber = MutableStateFlow("")
  val phoneNumber = _phoneNumber.asStateFlow()

  fun setPhone(number: String) = _phoneNumber.tryEmit(number)
    private val _currentUser = MutableStateFlow<UserResponse?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _otpRequestState = MutableStateFlow(UiState<OtpResponse>())
    val otpRequestState = _otpRequestState.asStateFlow()
    fun requestOtp() {
        launchWithState(
            stateFlow = _otpRequestState,
            block = { authRepo.requestOtp(_phoneNumber.value) },
            onSuccess = { otpData ->
                _otpRequestState.value = UiState(data = otpData)
            },
            onFailure = { error ->
                _otpRequestState.value = UiState(error = error.message ?: "Failed to request OTP")
            }
        )
    }

    private val _verifyOtpState = MutableStateFlow(UiState<VerifyOtpResponse>())
    val verifyOtpState = _verifyOtpState.asStateFlow()
    fun verifyOtp(phone: String, otp: String) {
        launchWithState(
            stateFlow = _verifyOtpState,
            block = { authRepo.verifyOtp(phone, otp) },
            onSuccess = { resp ->
                _verifyOtpState.value = UiState(data = resp)
                _currentUser.value = resp?.user
                _isLoggedIn.value = true
            },
            onFailure = { error ->
                _verifyOtpState.value = UiState(error = error.message ?: "Failed to verify OTP")
            }
        )
    }

    private val _logoutState = MutableStateFlow(UiState<EmptyResponse>())
    val logoutState = _logoutState.asStateFlow()
    fun logout() {
        launchWithState(
            stateFlow = _logoutState,
            block = { authRepo.logout() },
            onSuccess = {
                _currentUser.value = null
                _isLoggedIn.value = false
            }
        )
    }

    fun checkLoginStatus() {
        _isLoggedIn.value = authRepo.getToken() != null
    }

    
    fun resetStates() {
        _otpRequestState.value = UiState()
        _verifyOtpState.value = UiState()
        _logoutState.value = UiState()
    }
}
