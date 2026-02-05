package com.shopwallet.shopwallet.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.shopwallet.shopwallet.data.local.AppPreferenceManager

class SecurityViewModel(private val prefs: AppPreferenceManager) : ViewModel() {

    var isLocked by mutableStateOf(false)
        private set

    var hasPinSet by mutableStateOf(prefs.userPin != null)
        private set

    fun onAppForegrounded() {
        if (prefs.shouldShowPinScreen()) {
            isLocked = true
        }
    }

    fun onAppBackgrounded() {
        prefs.updateLastActiveTime()
    }

    fun unlock(pin: String): Boolean {
        return if (prefs.userPin == pin) {
            isLocked = false
            prefs.updateLastActiveTime()
            true
        } else {
            false
        }
    }

    fun setPin(pin: String) {
        prefs.userPin = pin
        hasPinSet = true
        isLocked = false
        prefs.updateLastActiveTime()
    }
}
