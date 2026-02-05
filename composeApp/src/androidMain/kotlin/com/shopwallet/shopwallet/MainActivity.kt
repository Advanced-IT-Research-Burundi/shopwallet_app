package com.shopwallet.shopwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    val settings = com.russhwolf.settings.SharedPreferencesSettings(
        getSharedPreferences("shopwallet_prefs", android.content.Context.MODE_PRIVATE)
    )

    setContent {
      App(settings)
    }
  }
}