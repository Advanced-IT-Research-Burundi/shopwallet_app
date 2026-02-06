package com.shopwallet.shopwallet

import android.app.Application
import com.shopwallet.shopwallet.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ShopWalletApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@ShopWalletApp)
        }
    }
}
