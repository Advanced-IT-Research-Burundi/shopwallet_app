package com.shopwallet.shopwallet.ui.viewmodel

import com.shopwallet.shopwallet.data.local.AppPreferenceManager
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SecurityViewModel(private val prefs: AppPreferenceManager) : androidx.lifecycle.ViewModel() {

    private val _isLocked = MutableStateFlow(false)
    val isLocked = _isLocked.asStateFlow()

    private val _hasPinSet = MutableStateFlow(prefs.userPin != null)
    val hasPinSet = _hasPinSet.asStateFlow()

    fun onAppForegrounded() {
        if (prefs.shouldShowPinScreen()) {
            _isLocked.value = true
        }
    }

    fun onAppBackgrounded() {
        prefs.updateLastActiveTime()
    }

    fun unlock(pin: String): Boolean {
        return if (prefs.userPin == pin) {
            _isLocked.value = false
            prefs.updateLastActiveTime()
            true
        } else {
            false
        }
    }

    fun setPin(pin: String) {
        prefs.userPin = pin
        _hasPinSet.value = true
        _isLocked.value = false
        prefs.updateLastActiveTime()
    }
}
