package com.shopwallet.shopwallet.notification

import com.mmk.kmpnotifier.notification.NotifierManager

class NotificationService {

    fun showValueNotification(value: String) {
        val notifier = NotifierManager.getLocalNotifier()
        notifier.notify("Notification", value)
    }

    suspend fun askPermission() {
        val permissionUtil = NotifierManager.getPermissionUtil()
        permissionUtil.askNotificationPermission()
    }
}
