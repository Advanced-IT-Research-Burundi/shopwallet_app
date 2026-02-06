package com.shopwallet.shopwallet

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { 
    val settings = com.russhwolf.settings.NSUserDefaultsSettings(
        platform.Foundation.NSUserDefaults.standardUserDefaults
    )
    App(settings) 
}