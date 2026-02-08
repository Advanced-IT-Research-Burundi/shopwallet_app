package com.shopwallet.shopwallet.utils

import android.util.Log

actual object Logger {
  private const val  TAG = "shopwallet"
  actual fun d(subTag: String, body: String) {
    Log.d(TAG, "[$subTag]: $body")
  }

  actual fun e(subTag: String, body: String, throwable: Throwable?) {
    if (throwable != null) Log.e(TAG, "[$subTag]: $body", throwable) else Log.e(TAG, "[$subTag]: $body")
  }

  actual fun w(subTag: String, body: String) {
    Log.w(TAG, "[$subTag]: $body")
  }
}