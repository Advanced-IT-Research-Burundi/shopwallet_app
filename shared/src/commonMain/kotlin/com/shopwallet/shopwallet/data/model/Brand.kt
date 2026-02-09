package com.shopwallet.shopwallet.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Brand(
  val id: Int,
  val name: String,
  val description: String,
  val logo: String,
  @SerialName("cover_image") val coverImage: String,
  val color: String = "#6200EE"
)
