package com.shopwallet.shopwallet.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule, viewModelModule)
    }

// called by iOS
fun initKoin() = initKoin {}
