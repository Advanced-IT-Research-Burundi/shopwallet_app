package com.shopwallet.shopwallet.data.model

data class Product(
  val id: String,
  val brandId: String,
  val name: String,
  val description: String,
  val price: Double,
  val category: String,
  val image: String
)