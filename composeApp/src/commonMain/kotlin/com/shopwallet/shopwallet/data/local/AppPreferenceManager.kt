package com.shopwallet.shopwallet.data.local

import com.russhwolf.settings.Settings
import com.shopwallet.shopwallet.data.model.CartItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

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

    /**
     * Gets current monotonic time in milliseconds.
     * This represents time passed since the device started.
     */
    private fun currentMonotonicMs(): Long =
        TimeSource.Monotonic.markNow().elapsedNow().inWholeMilliseconds

    fun updateLastActiveTime() {
        // Save the current "tick" count
        settings.putLong(KEY_LAST_ACTIVE_TIME, currentMonotonicMs())
    }

    fun shouldShowPinScreen(): Boolean {
        if (userPin == null) return false

        val lastActiveTick = settings.getLong(KEY_LAST_ACTIVE_TIME, 0L)
        if (lastActiveTick == 0L) return true

        val currentTick = currentMonotonicMs()
        val diff = currentTick - lastActiveTick

        // Use .minutes for readability
        return diff > 5.minutes.inWholeMilliseconds
    }
    
    fun logout() {
        settings.remove(KEY_AUTH_TOKEN)
        settings.remove(KEY_IS_LOGGED_IN)
    }
}
