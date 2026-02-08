package com.shopwallet.shopwallet.utils

expect object Logger {
  fun d(subTag: String, body: String)
  fun e(subTag: String, body: String, throwable: Throwable? = null)
  fun w(subTag: String, body: String)

}