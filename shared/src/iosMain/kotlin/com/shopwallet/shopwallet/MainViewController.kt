package com.shopwallet.shopwallet

import androidx.compose.ui.window.ComposeUIViewController
import com.russhwolf.settings.NSUserDefaultsSettings
import platform.Foundation.NSUserDefaults

fun MainViewController() = ComposeUIViewController { 
    val settings = NSUserDefaultsSettings(
        NSUserDefaults.standardUserDefaults
    )
    App()
}