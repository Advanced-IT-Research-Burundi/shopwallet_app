package com.shopwallet.shopwallet

import android.app.Application
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.shopwallet.shopwallet.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ShopWalletApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // initialize notification
        NotifierManager.initialize(
            NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_notification,
                showPushNotification = true // Optional
            )
        )

        initKoin {
            androidLogger()
            androidContext(this@ShopWalletApp)
        }
    }

}
