package com.shopwallet.shopwallet.data.local

import com.russhwolf.settings.Settings
import com.shopwallet.shopwallet.data.model.CartItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

/**
 * AppPreferenceManager handles all local persistence logic.
 * It is initialized with a Settings instance provided by the platform.
 * This class is pure KMP and avoids expect/actual by taking Settings as a parameter.
 */
class AppPreferenceManager(val settings: Settings) {

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_PIN = "user_pin"
        private const val KEY_LAST_ACTIVE_TIME = "last_active_time"
        private const val KEY_CART_PREFIX = "cart_"
        
        private val json = Json { ignoreUnknownKeys = true }
    }

    // Auth persistence
    var authToken: String?
        get() = settings.getStringOrNull(KEY_AUTH_TOKEN)
        set(value) { 
            if (value != null) settings.putString(KEY_AUTH_TOKEN, value)
            else settings.remove(KEY_AUTH_TOKEN)
        }

    var isLoggedIn: Boolean
        get() = settings.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) { settings.putBoolean(KEY_IS_LOGGED_IN, value) }

    // Brand-specific Cart persistence
    fun saveCart(brandId: String, items: List<CartItem>) {
        val serialized = json.encodeToString<List<CartItem>>(items)
        settings.putString("$KEY_CART_PREFIX$brandId", serialized)
    }

    fun getCart(brandId: String): List<CartItem> {
        val serialized = settings.getStringOrNull("$KEY_CART_PREFIX$brandId") ?: return emptyList()
        return try {
            json.decodeFromString<List<CartItem>>(serialized)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // PIN Security
    var userPin: String?
        get() = settings.getStringOrNull(KEY_USER_PIN)
        set(value) { 
            if (value != null) settings.putString(KEY_USER_PIN, value)
            else settings.remove(KEY_USER_PIN)
        }

    fun updateLastActiveTime() {
        settings.putLong(KEY_LAST_ACTIVE_TIME, Clock.System.now().toEpochMilliseconds())
    }

    fun shouldShowPinScreen(): Boolean {
        if (userPin == null) return false

        val lastActiveMs = settings.getLong(KEY_LAST_ACTIVE_TIME, 0L)
        if (lastActiveMs == 0L) return true

        val now = Clock.System.now().toEpochMilliseconds()
        val diff = now - lastActiveMs

        // Use .minutes for readability
        return diff > 5.minutes.inWholeMilliseconds
    }
    
    fun logout() {
        settings.remove(KEY_AUTH_TOKEN)
        settings.remove(KEY_IS_LOGGED_IN)
    }
}
