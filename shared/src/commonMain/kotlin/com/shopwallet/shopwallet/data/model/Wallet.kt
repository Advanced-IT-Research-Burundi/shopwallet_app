package com.shopwallet.shopwallet.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wallet(
  val id: Int,
  @SerialName("subscription_id") val subscriptionId: Int,
  val balance: String
)