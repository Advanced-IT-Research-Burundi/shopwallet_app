package com.shopwallet.shopwallet

import androidx.compose.ui.window.ComposeUIViewController
import com.russhwolf.settings.NSUserDefaultsSettings
import platform.Foundation.NSUserDefaults
import com.shopwallet.shopwallet.di.initKoin

fun startKoinIOS() {
    initKoin()
}

fun MainViewController() = ComposeUIViewController { 
    val settings = NSUserDefaultsSettings(
        NSUserDefaults.standardUserDefaults
    )
    App()
}