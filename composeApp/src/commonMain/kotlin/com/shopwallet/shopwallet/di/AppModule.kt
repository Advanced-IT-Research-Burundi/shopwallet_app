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
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {
    single<Settings> { Settings() }
    single { AppPreferenceManager(get()) }
    
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }
    
    // Repositories
    single<AuthRepo> { AuthRepoImpl(get(), get()) }
    single<BrandRepo> { BrandRepoImpl(get()) }
    single<WalletRepo> { WalletRepoImpl(get()) }
}
