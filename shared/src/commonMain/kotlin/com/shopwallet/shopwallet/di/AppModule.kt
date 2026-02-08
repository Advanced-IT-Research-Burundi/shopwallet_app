package com.shopwallet.shopwallet.di

import com.russhwolf.settings.Settings
import com.shopwallet.shopwallet.data.local.AppPreferenceManager
import com.shopwallet.shopwallet.data.remote.KtorClient
import com.shopwallet.shopwallet.data.remote.provideHttpClient
import com.shopwallet.shopwallet.data.repository.AuthRepo
import com.shopwallet.shopwallet.data.repository.AuthRepoImpl
import com.shopwallet.shopwallet.data.repository.BrandRepo
import com.shopwallet.shopwallet.data.repository.BrandRepoImpl
import com.shopwallet.shopwallet.data.repository.WalletRepo
import com.shopwallet.shopwallet.data.repository.WalletRepoImpl
import com.shopwallet.shopwallet.notification.NotificationService
import org.koin.dsl.module

val appModule = module {
    // Local Data
    single<Settings> { Settings() }
    single { AppPreferenceManager(get()) }
    
    // Network
    val baseUrl = "https://shopping-wallet.advanceditb.com/api"
    single { provideHttpClient(get()) }

    // API Clients
    single { KtorClient(get(), baseUrl) }
    
    // Repositories
    single<AuthRepo> { AuthRepoImpl(get(), get()) }
    single<BrandRepo> { BrandRepoImpl(get()) }
    single<WalletRepo> { WalletRepoImpl(get()) }

    // Services
    single { NotificationService() }
}
