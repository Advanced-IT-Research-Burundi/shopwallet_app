package com.shopwallet.shopwallet.data.remote

import com.shopwallet.shopwallet.data.local.AppPreferenceManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun provideHttpClient(prefs: AppPreferenceManager): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        install(Logging) {
            logger = io.ktor.client.plugins.logging.Logger.DEFAULT
            level = LogLevel.INFO
        }

        defaultRequest {
            // Add auth token if available
            val token = prefs.authToken
            if (!token.isNullOrEmpty()) {
                headers.append("Authorization", "Bearer $token")
            }
        }
    }
}
