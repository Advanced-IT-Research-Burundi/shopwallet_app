package com.shopwallet.shopwallet.di

import com.shopwallet.shopwallet.ui.viewmodel.AuthViewModel
import com.shopwallet.shopwallet.ui.viewmodel.BrandViewModel
import com.shopwallet.shopwallet.ui.viewmodel.SecurityViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { AuthViewModel(get()) }
    factory { (brandId: String) -> BrandViewModel(brandId, get()) }
    factory { SecurityViewModel(get()) }
}
