package com.shopwallet.shopwallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopwallet.shopwallet.data.model.User
import com.shopwallet.shopwallet.data.repository.AuthRepo
import com.shopwallet.shopwallet.utils.UiState
import com.shopwallet.shopwallet.utils.launchWithState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepo: AuthRepo
) : androidx.lifecycle.ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(authRepo.getUser())
    val currentUser = _currentUser.asStateFlow()

    private val _loginState = MutableStateFlow(UiState<User>())
    val loginState = _loginState.asStateFlow()

    fun login(email: String, pin: String) {
        viewModelScope.launch {
            launchWithState(
                stateFlow = _loginState,
                block = {
                    // Mock login for now
                    if (email.isNotEmpty() && pin.length == 4) {
                        val mockUser = User(id = "1", name = "Test User", email = email)
                        Result.success(mockUser)
                    } else {
                        Result.failure(Exception("Invalid credentials"))
                    }
                },
                onSuccess = { user ->
                    authRepo.saveUser(user)
                    _currentUser.value = user
                }
            )
        }
    }

    fun logout() {
        authRepo.clearSession()
        _currentUser.value = null
    }
}
