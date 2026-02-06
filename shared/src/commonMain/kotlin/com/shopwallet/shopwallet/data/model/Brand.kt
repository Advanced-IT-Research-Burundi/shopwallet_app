package com.shopwallet.shopwallet.data.model

data class Brand(
  val id: String,
  val name: String,
  val description: String,
  val logo: String,
  val coverImage: String,
  val primaryColor: String = "#6200EE" // Default primary
)