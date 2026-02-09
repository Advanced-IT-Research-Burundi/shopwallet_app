package com.shopwallet.shopwallet.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
  val id: Int,
  @SerialName("user_id") val userId: Int,
  @SerialName("company_id") val companyId: Int,
  val status: String,
  val company: Brand,
  val wallet: Wallet
)