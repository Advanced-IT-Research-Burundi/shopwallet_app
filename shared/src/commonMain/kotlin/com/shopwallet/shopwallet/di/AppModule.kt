package com.shopwallet.shopwallet.di

import com.russhwolf.settings.Settings
import com.shopwallet.shopwallet.data.local.AppPreferenceManager
import com.shopwallet.shopwallet.data.remote.AuthClient
import com.shopwallet.shopwallet.data.remote.BrandClient
import com.shopwallet.shopwallet.data.remote.WalletClient
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import com.shopwallet.shopwallet.data.repository.AuthRepo
import com.shopwallet.shopwallet.data.repository.AuthRepoImpl
import com.shopwallet.shopwallet.data.repository.BrandRepo
import com.shopwallet.shopwallet.data.repository.BrandRepoImpl
import com.shopwallet.shopwallet.data.repository.WalletRepo
import com.shopwallet.shopwallet.data.repository.WalletRepoImpl
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {
    // Local Data
    single<Settings> { Settings() }
    single { AppPreferenceManager(get()) }
    
    // Network
    single<HttpClient> {
        val prefs = get<AppPreferenceManager>()
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            
            install(io.ktor.client.plugins.logging.Logging) {
                logger = io.ktor.client.plugins.logging.Logger.DEFAULT
                level = io.ktor.client.plugins.logging.LogLevel.INFO
            }
            
            defaultRequest {
                url("https://shopping-wallet.advanceditb.com/api/")
                
                // Add auth token if available
                val token = prefs.authToken
                if (!token.isNullOrEmpty()) {
                    headers.append("Authorization", "Bearer $token")
                }
            }
        }
    }

    // API Clients
    single { AuthClient(get()) }
    single { BrandClient(get()) }
    single { WalletClient(get()) }
    
    // Repositories
    single<AuthRepo> { AuthRepoImpl(get(), get()) }
    single<BrandRepo> { BrandRepoImpl(get()) }
    single<WalletRepo> { WalletRepoImpl(get()) }

    // Services
    single { com.shopwallet.shopwallet.notification.NotificationService() }
}
