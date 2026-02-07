package com.shopwallet.shopwallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopwallet.shopwallet.notification.NotificationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val notificationService: NotificationService
) : ViewModel() {

    fun triggerNotification() {
        viewModelScope.launch {
            // Simulate API call
            delay(2000)
            notificationService.showValueNotification("Data received from backend!")
        }
    }
    
    fun askNotificationPermission() {
        viewModelScope.launch {
            notificationService.askPermission()
        }
    }
}
