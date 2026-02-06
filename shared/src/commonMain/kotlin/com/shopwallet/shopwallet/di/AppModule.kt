package com.shopwallet.shopwallet.di

import com.russhwolf.settings.Settings
import com.shopwallet.shopwallet.data.local.AppPreferenceManager
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
    single<Settings> { Settings() }
    single { AppPreferenceManager(get()) }
    
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
    
    // Repositories
    single<AuthRepo> { AuthRepoImpl(get(), get()) }
    single<BrandRepo> { BrandRepoImpl(get()) }
    single<WalletRepo> { WalletRepoImpl(get()) }
}
