package com.shopwallet.shopwallet.di

import com.shopwallet.shopwallet.ui.viewmodel.AuthViewModel
import com.shopwallet.shopwallet.ui.viewmodel.BrandViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { AuthViewModel(get(), get()) }
    factory { (brandId: String) -> BrandViewModel(brandId, get()) }
}
