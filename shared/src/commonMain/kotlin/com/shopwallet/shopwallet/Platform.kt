package com.shopwallet.shopwallet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform