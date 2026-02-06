package com.shopwallet.shopwallet.utils

object CurrencyFormat {

  fun doubleToBif(value: Double): String {
    val longValue = value.toLong()
    val str = longValue.toString()
    val sb = StringBuilder()

    val firstGroupLength = str.length % 3
    for (i in str.indices) {
      if (i > 0 && (i - firstGroupLength) % 3 == 0) {
        sb.append(' ')
      }
      sb.append(str[i])
    }

    return sb.toString()
  }

  fun bifToDouble(value: String): Double {
    val digits = value.filter { it.isDigit() }
    return digits.toDoubleOrNull() ?: 0.0
  }
}
